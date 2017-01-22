package pl.bka.model

import pl.bka.JsonProtocols

trait InternalMessage

object InternalMessage extends JsonProtocols {
  import spray.json._

  def toJson(msg: InternalMessage): String = msg match {
    case message: WindowWordCounts => message.toJson.toString
    case message: HeartBeat => message.toJson.toString
  }
}