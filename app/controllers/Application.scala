package controllers

import actors.ws.{ExampleWs, StatefullExampleWs}
import play.api._
import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {
  
  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index("Choose one"))
  }
  
  def indexStateless: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.indexStateless("Welcome! Let's Play with your brand new stateless websocket!")).withSession(
        ("uuid" -> java.util.UUID.randomUUID.toString)
      )
  }
  def indexStatefull: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.indexStatefull("Welcome! Let's Play with your brand new statefull websocket!")).withSession(
        ("uuid" -> java.util.UUID.randomUUID.toString)
      )
  }
  
  def ws: WebSocket[JsValue] = WebSocket.async[JsValue] {
    (request: RequestHeader) =>
	  ExampleWs.control(request)
  }(WebSocket.FrameFormatter.jsonFrame)
  
  def statefullWs: WebSocket[JsValue] =
    WebSocket.async[JsValue] {implicit request => 
	  StatefullExampleWs.control
  }
  
  def testBroadCast: Action[AnyContent] = Action {
    ExampleWs.actor ! ExampleWs.AlertForSomething
    Ok("Msg2 Sent")
  }
  
  def testBroadCastStatefull: Action[AnyContent] = Action {
    StatefullExampleWs.actor ! StatefullExampleWs.AlertForSomething
    Ok("Msg2 Sent to statefull")
  }
}