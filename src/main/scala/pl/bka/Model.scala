package pl.bka

sealed trait Message

case class WindowWordCounts(counts: List[(Int, String)]) extends Message

object WindowWordCounts {
  def zero: WindowWordCounts = WindowWordCounts(List((0, "")))
}

object Message extends JsonProtocols {
  import spray.json._

  def toJson(msg: Message): String = msg match {
    case message: WindowWordCounts => message.toJson.toString
  }
}

