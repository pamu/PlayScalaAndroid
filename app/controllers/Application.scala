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
