package controllers

import play.api.mvc.{Action, Controller}


object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to LimoService."))
  }

  def login() = Action { implicit request =>
    Ok("")
  }

  def signup() = Action { implicit request =>
    Ok("")
  }

 }
