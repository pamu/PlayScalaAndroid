package global

import actors.CarStore
import akka.actor.{Props, ActorSystem}
import play.api.GlobalSettings
import play.api.Application

/**
 * Created by pnagarjuna on 01/05/15.
 */
object Global extends GlobalSettings {
  val system = ActorSystem("PlayScalaAndroid")
  lazy val carStore = system.actorOf(Props[CarStore], name = "CarStore")

  override def onStart(app: Application): Unit = {
    super.onStart(app)
  }
  override def onStop(app: Application): Unit = {
    super.onStop(app)
  }
}
