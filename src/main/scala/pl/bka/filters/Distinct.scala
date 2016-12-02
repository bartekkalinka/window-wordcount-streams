package pl.bka.filters

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Distinct {
  def distinct[T](zero: T): Flow[T, T, NotUsed] =
    Flow[T]
      .scan((zero, zero)) { case ((prevprev, prev), current) => (prev, current)}
      .filter { case (prev, current) => prev != current }
      .map(_._2)
}

