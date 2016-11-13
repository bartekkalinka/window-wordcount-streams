import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}

import scala.concurrent.duration._

object Main {
  def textFileWordsSource(path: String, interval: FiniteDuration) = {
    val fileSource = FileIO.fromPath(Paths.get(path))
    fileSource
      .map(_.utf8String)
      .flatMapConcat(longStr => Source[String](longStr.split(Array(' ', '\n', '\t')).toList))
      .filter(_.trim.length > 0)
      .throttle(1, interval, 1, ThrottleMode.Shaping)
  }

  def windowTopWords(windowSize: Int, minLength: Int = 1): Flow[String, (Int, String), NotUsed] =
    Flow[String]
      .sliding(windowSize).map { window =>
        val bestWordSeq =
          window.filter(_.length >= minLength).groupBy(identity).values.toSeq.sortBy(_.length).reverse.head
        (bestWordSeq.length, bestWordSeq.head)
      }

  def distinct[T](zero: T): Flow[T, T, NotUsed] =
    Flow[T]
      .scan((zero, zero)) { case ((prevprev, prev), current) => (prev, current)}
      .filter { case (prev, current) => prev != current }
      .map(_._2)

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    textFileWordsSource("input3.txt", 10.millis)
      .via(windowTopWords(50, 3))
      .via(distinct((0, "")))
      .runWith(Sink.foreach(println))
  }
}

