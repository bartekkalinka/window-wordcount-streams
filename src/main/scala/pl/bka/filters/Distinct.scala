package pl.bka.filters

import akka.NotUsed
import akka.stream.scaladsl.Flow
import pl.bka.model.WindowWordCounts

object Distinct {
  def window = distinct[List[(Int, String)], WindowWordCounts](WindowWordCounts.zero, _.counts)

  private def distinct[T, S](zero: S, extract: S => T): Flow[S, S, NotUsed] =
    Flow[S]
      .scan((zero, zero)) { case ((_, prev), current) => (prev, current)}
      .filter { case (prev, current) => extract(prev) != extract(current) }
      .map(_._2)
}

