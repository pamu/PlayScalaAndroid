package actors

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
  case object NotFound
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
      if (cars contains name) {
        sender ! cars(name)
      } else {
        sender ! Client.NotFound
      }
  }
}
