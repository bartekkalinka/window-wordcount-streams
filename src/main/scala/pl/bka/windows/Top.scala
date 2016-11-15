package pl.bka.windows

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Top {
  def wordWithOccurence(windowSize: Int, minLength: Int = 1): Flow[String, (Int, String), NotUsed] =
    Flow[String]
      .sliding(windowSize).map { window =>
      val bestWordSeq =
        window.filter(_.length >= minLength).groupBy(identity).values.toSeq.sortBy(_.length).reverse.head
      (bestWordSeq.length, bestWordSeq.head)
    }
}

