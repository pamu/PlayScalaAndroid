# PlayScalaAndroid
Play framework backend to the ScalaAndroid app written in Scala.

### Query
```
    http://playscalaandroid.herokuapp.com/info?car=swift
```

### Response
```json
    {"name":"swift","url":"http://i.ndtvimg.com/auto/makers/29/205/suzuki-swift-2011-front-view.jpg"}
```

### Query

```
    http://playscalaandroid.herokuapp.com/info?car=blah
```

### Response

```
    {"name":"not_found","url":"not_found"}
```

## Architecture

![Play Architecture](https://raw.githubusercontent.com/pamu/PlayScalaAndroid/master/images/play.png)

# Code

### CarStore Actor

```scala

    package actors

    import actors.Client.Car
    import akka.actor.{ActorLogging, Actor}

    /**
     * Created by pnagarjuna on 01/05/15.
     */

    object CarStore {
      case class AddCar(name: String, url: String)
      case class RemoveCar(name: String)
      case class GetCar(name: String)
    }

    object Client {
      trait Result
      case object NotFound extends Result
      case class Car(name: String, url: String) extends Result
      case class CarInfoHtml(name: String, html: String) extends Result
      case class CarInfo(name: String, text: String) extends Result
    }

    class CarStore extends Actor with ActorLogging {
      var cars = Map.empty[String, String]
      var info = Map.empty[String, String]

      import CarStore._
      override def receive = {
        case AddCar(name, url) =>
          if (!(cars contains name)) {
            cars += (name -> url)
          }
        case RemoveCar(name) =>
          if (cars contains name) {
            cars -= name
          }
        case GetCar(name) =>
          log.info(s"car request: car $name")
          if (cars contains name) {
            sender ! Car(name, cars(name))
            log.info(s"sent ${cars(name)}")
          } else {
            sender ! Client.NotFound
          }
      }
    }
```

### Application Controller

```scala

    package controllers

    import actors.Client
    import actors.Client.{Car, Result}
    import akka.util.Timeout
    import global.Global
    import play.api.Logger
    import play.api.libs.json.{Json, JsPath, Writes}
    import play.api.mvc.{Action, Controller}
    import play.api.libs.functional.syntax._

    import scala.concurrent.Future

    import akka.pattern.ask

    import scala.concurrent.duration._

    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    object Application extends Controller {

      def index = Action {
        Ok(views.html.index("Welcome to PlayScalaAndroid"))
      }

      implicit val writes: Writes[Client.Car] = (
        (JsPath \ "name").write[String] and
          (JsPath \ "url").write[String]
        ) (unlift(Client.Car.unapply _))

      def info(car: String) = Action.async { implicit request =>
        import actors.CarStore._
        implicit val timeout = Timeout(5 seconds)
        val future: Future[Result] = (Global.carStore ? GetCar(car)).mapTo[Result]
        future.map { result => {
          result match {
            case car: Car => Ok(Json.toJson(car))
            case Client.NotFound => Ok(Json.toJson(Car("not_found", "not_found")))
          }
        }
        }.recover{case throwable => Ok(Json.toJson(Car("not_found", "not_found")))}
      }

     }
```

### Global

```scala

    package global

    import actors.CarStore
    import akka.actor.{Props, ActorSystem}
    import play.api.{Logger, GlobalSettings, Application}

    /**
     * Created by pnagarjuna on 01/05/15.
     */
    object Global extends GlobalSettings {
      val system = ActorSystem("PlayScalaAndroid")
      lazy val carStore = system.actorOf(Props[CarStore], name = "CarStore")

      override def onStart(app: Application): Unit = {
        super.onStart(app)
        Logger.info("PlayScalaAndroid Started")
        import actors.CarStore._
        carStore ! AddCar(name = "swift", url = "http://i.ndtvimg.com/auto/makers/29/205/suzuki-swift-2011-front-view.jpg")
        carStore ! AddCar(name = "maruti", url = "http://i.ndtvimg.com/auto/makers/29/196/2012-maruti-suzuki-ertiga-001-1570.jpg")
        carStore ! AddCar(name = "tesla", url = "http://image.motortrend.com/f/oftheyear/car/1301_2013_motor_trend_car_of_the_year_tesla_model_s/41007734/2013-tesla-model-s-front-1.jpg")
        carStore ! AddCar(name = "bmw", url = "http://2015bestsuvreviews.com/wp-content/uploads/2014/08/2017-BMW-X3-front.jpg")
        carStore ! AddCar(name = "nano", url = "http://www.igyaan.in/wp-content/uploads/2013/06/tata-nano-exterior-047.jpg")
      }
      override def onStop(app: Application): Unit = {
        super.onStop(app)
        Logger.info("PlayScalaAndroid Stopped")
      }
    }
```

### routes

```
    # Routes
    # This file defines all application routes (Higher priority routes first)
    # ~~~~

    GET     /                           controllers.Application.index()
    GET     /info                       controllers.Application.info(car: String)


    # Map static resources from the /public folder to the /assets URL path
    GET     /assets/*file               controllers.Assets.at(path="/public", file)
    GET     /webjars/*file              controllers.WebJarAssets.at(file)
```