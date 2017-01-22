package pl.bka.model

import pl.bka.JsonProtocols

trait Message

object Message extends JsonProtocols {
  import spray.json._

  def toJson(msg: Message): String = msg match {
    case message: WindowWordCounts => message.toJson.toString
    case message: HeartBeat => message.toJson.toString
  }
}