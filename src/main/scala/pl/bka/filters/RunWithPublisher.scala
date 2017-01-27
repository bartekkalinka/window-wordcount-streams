package pl.bka.filters

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, Sink, Source}

object RunWithPublisher {
  def source[A, M](normal: Source[A, M])(implicit fm: Materializer, system: ActorSystem): (Source[A, NotUsed], M) = {
    val (normalMat, publisher) = normal.toMat(Sink.asPublisher(fanout = true))(Keep.both).run
    (Source.fromPublisher(publisher), normalMat)
  }
}

