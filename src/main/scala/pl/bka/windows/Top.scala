package pl.bka.windows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import pl.bka.model.WindowWordCounts

object Top {
  def nwordsSliding(windowSize: Int, topWordsNum: Int, minWordLength: Int): Flow[(String, Long), WindowWordCounts, NotUsed] =
    Flow[(String, Long)].sliding(windowSize).scan((Map[String, Int](), 0L)) { case ((map, _), window) =>
        val out = window.head._1
        val in = window.last._1
        def outUpdated(m: Map[String, Int]) = m.get(out).map(_ - 1)
        def inUpdated(m: Map[String, Int]) = m.get(in).map(_ + 1).orElse(Some(1)).filter(_ => in.length >= minWordLength)
        def updateOut(m: Map[String, Int]) = outUpdated(m).map(upd => if(upd == 0) m - out else m.updated(out, upd)).getOrElse(m)
        def updateIn(m: Map[String, Int]) = inUpdated(m).map(upd => m + (in -> upd)).getOrElse(m)
        val interval = window.last._2 - window.head._2
      (updateIn(updateOut(map)), interval)
    }.map { case (m, interval) =>
      val list = m.toSeq.sortBy(_._2).reverse.take(topWordsNum).map(kv => (kv._2, kv._1)).toList
      WindowWordCounts(list, interval)
    }
}

