package pl.bka.soruces

import akka.NotUsed
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source

import scala.concurrent.duration.FiniteDuration

object TextFileSource {
  def words(path: String, interval: FiniteDuration): Source[String, NotUsed] = {
    val fileSource = scala.io.Source.fromFile(path)
    Source.fromIterator(fileSource.getLines)
      .flatMapConcat(longStr => Source[String](longStr.split(Array(' ', '\n', '\t')).toList))
      .filter(_.trim.length > 0)
      .throttle(1, interval, 1, ThrottleMode.Shaping)
  }
}