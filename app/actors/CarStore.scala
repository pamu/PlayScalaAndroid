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
}

class CarStore extends Actor with ActorLogging {
  var cars = Map.empty[String, String]
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
