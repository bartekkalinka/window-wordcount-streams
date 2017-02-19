package pl.bka.displays

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}

case class WebsocketDisplay(dataSource: Source[String, NotUsed])(implicit fm: Materializer, system: ActorSystem) extends Directives {
  private def route =
    pathSingleSlash {
      getFromResource("client/index.html")
    } ~
    path("window-streams") {
      parameter('clientId) { clientId =>
        handleWebSocketMessages(websocketFlow(sender = clientId))
      }
    } ~
      getFromResourceDirectory("client")

  private def websocketFlow(sender: String): Flow[Message, Message, NotUsed] =
    Flow[Message]
      .collect { case TextMessage.Strict(msg) => msg }
      .via(logicFlow)
      .map{ msg: String => TextMessage.Strict(msg) }

  private def logicFlow: Flow[String, String, NotUsed] = Flow.fromSinkAndSource(Sink.ignore, dataSource)

  def bind() = {
    Http().bindAndHandle(route, "0.0.0.0", 9000)
  }
}

