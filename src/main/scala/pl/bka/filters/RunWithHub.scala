package pl.bka.filters

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Keep, Source}

object RunWithHub {
  def source[A, M](normal: Source[A, M])(implicit fm: Materializer, system: ActorSystem): (Source[A, NotUsed], M) = {
    val (normalMat, hubSource) = normal.toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both).run
    (hubSource, normalMat)
  }
}

