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
