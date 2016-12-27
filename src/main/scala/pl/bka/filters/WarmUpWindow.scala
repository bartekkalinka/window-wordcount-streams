package pl.bka.filters

import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.util.Random

object WarmUpWindow {
  def fakeWords(realWords: Source[String, NotUsed], warmupSize: Int, wordLength: Int = 5): Source[String, NotUsed] = {
    val fakeSeq: List[String] = List.tabulate(warmupSize)(_ => Random.nextString(wordLength))
    Source[String](fakeSeq).concat(realWords)
  }
}

