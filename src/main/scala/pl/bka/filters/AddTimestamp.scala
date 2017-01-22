package pl.bka.filters

import akka.NotUsed
import akka.stream.scaladsl.Flow

object AddTimestamp {
  def flow: Flow[String, (String, Long), NotUsed] =
    Flow[String].map((_, System.currentTimeMillis()))
}
