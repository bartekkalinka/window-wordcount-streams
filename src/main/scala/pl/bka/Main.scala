package pl.bka

import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import akka.stream.ActorMaterializer
import pl.bka.soruces.TextFileSource
import pl.bka.windows.Top

import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    TextFileSource.words("input3.txt", 10.millis)
      .via(Top.wordWithOccurence(50, 3))
      .via(Distinct.distinct((0, "")))
      .runWith(Sink.foreach(println))
  }
}

