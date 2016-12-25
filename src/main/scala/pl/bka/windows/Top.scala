package pl.bka.windows

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Top {
  def wordWithOccurence(windowSize: Int, minWordLength: Int = 1): Flow[String, (Int, String), NotUsed] =
    Flow[String]
      .sliding(windowSize).map { window =>
      val bestWordSeq =
        window.filter(_.length >= minWordLength).groupBy(identity).values.toSeq.sortBy(_.length).reverse.head
      (bestWordSeq.length, bestWordSeq.head)
    }

  def nwords(windowSize: Int, topWordsNum: Int, minWordLength: Int = 1): Flow[String, List[(Int, String)], NotUsed] =
    Flow[String]
      .sliding(windowSize).map { window =>
        window
          .filter(_.length >= minWordLength)
          .groupBy(identity).values.toSeq
          .sortBy(_.length).reverse.take(topWordsNum).toList
          .map { bestWordSeq => (bestWordSeq.length, bestWordSeq.head)}
    }
}

