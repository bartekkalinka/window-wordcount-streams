package pl.bka.windows

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Top {
  def nwordsSliding(windowSize: Int, topWordsNum: Int, minWordLength: Int = 1): Flow[String, List[(Int, String)], NotUsed] =
    sliding(windowSize, nwordsFunction(topWordsNum, minWordLength))

  def wordSliding(windowSize: Int, minWordLength: Int = 1): Flow[String, (Int, String), NotUsed] =
    sliding(windowSize, wordWitOccurenceFunction(minWordLength))

  private def sliding[A](windowSize: Int, windowFunction: Seq[String] => A): Flow[String, A, NotUsed] =
    Flow[String].sliding(windowSize).map(windowFunction)

  private def wordWitOccurenceFunction(minWordLength: Int): Seq[String] => (Int, String) = { window =>
    val bestWordSeq =
      window.filter(_.length >= minWordLength).groupBy(identity).values.toSeq.sortBy(_.length).reverse.head
    (bestWordSeq.length, bestWordSeq.head)
  }

  private def nwordsFunction(topWordsNum: Int, minWordLength: Int): Seq[String] => List[(Int, String)] = { window =>
    window
      .filter(_.length >= minWordLength)
      .groupBy(identity).values.toSeq
      .sortBy(_.length).reverse.take(topWordsNum).toList
      .map { bestWordSeq => (bestWordSeq.length, bestWordSeq.head)}
  }
}

