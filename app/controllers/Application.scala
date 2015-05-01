package controllers

import play.api.mvc.{Action, Controller}


object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to PlayScalaAndroid"))
  }

  def clientMessage(name: String, text: String) = Action { implicit request =>

  }

 }
