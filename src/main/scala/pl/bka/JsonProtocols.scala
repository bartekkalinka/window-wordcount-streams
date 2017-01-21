package pl.bka

import spray.json.DefaultJsonProtocol

trait JsonProtocols extends DefaultJsonProtocol {
  implicit val windowWordCountsFormat = jsonFormat1(WindowWordCounts.apply)
}

