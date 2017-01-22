package pl.bka.filters

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}

object RunWithPublisher {
  def source[A](normal: Source[A, NotUsed])(implicit fm: Materializer, system: ActorSystem): Source[A, NotUsed] =
    Source.fromPublisher(normal.toMat(Sink.asPublisher(fanout = true))((a, b) => b).run)
}

