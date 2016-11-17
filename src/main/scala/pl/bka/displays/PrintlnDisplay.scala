package pl.bka.displays

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}

case class PrintlnDisplay[A](dataSource: Source[A, NotUsed])(implicit fm: Materializer, system: ActorSystem) {
  def display() = dataSource.runWith(Sink.foreach(println))
}

