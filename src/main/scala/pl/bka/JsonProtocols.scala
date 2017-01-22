package pl.bka

import pl.bka.model.{HeartBeat, WindowWordCounts}
import spray.json.DefaultJsonProtocol

trait JsonProtocols extends DefaultJsonProtocol {
  implicit val windowWordCountsFormat = jsonFormat2(WindowWordCounts.apply)
  implicit val heartBeatFormat = jsonFormat1(HeartBeat.apply)
}

