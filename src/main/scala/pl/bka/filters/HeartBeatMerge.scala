package pl.bka.filters

import akka.NotUsed
import akka.stream.ThrottleMode
import akka.stream.scaladsl.{Flow, Source}
import pl.bka.model.{HeartBeat, InternalMessage}

import scala.concurrent.duration._

object HeartBeatMerge {
  def flow: Flow[InternalMessage, InternalMessage, NotUsed] = {
    val heartBeat = Source.fromIterator[HeartBeat](() => Iterator.continually(HeartBeat())).throttle(1, 1.seconds, 1, ThrottleMode.Shaping)
    Flow[InternalMessage].merge(heartBeat)
  }
}

