package pl.bka.sources

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{Materializer, ThrottleMode}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration.FiniteDuration

object DebugSource {
  def apply(topWordsNum: Int, interval: FiniteDuration, throughPublisher: Boolean)(implicit fm: Materializer, system: ActorSystem): Source[Seq[(Int, String)], NotUsed] = {
    val normal = normalSource(topWordsNum, interval)
    if(throughPublisher) sourceThroughPublisher(normal) else normal
  }

  private def normalSource(topWordsNum: Int, interval: FiniteDuration): Source[Seq[(Int, String)], NotUsed] =
    Source(Stream.iterate(0)(i => i + 1))
      .map(i => (i, i.toString))
      .sliding(topWordsNum)
      .throttle(1, interval, 1, ThrottleMode.Shaping)

  private def sourceThroughPublisher(normal: Source[Seq[(Int, String)], NotUsed])(implicit fm: Materializer, system: ActorSystem): Source[Seq[(Int, String)], NotUsed] =
    Source.fromPublisher(normal.toMat(Sink.asPublisher(fanout = true))((a, b) => b).run)
}

