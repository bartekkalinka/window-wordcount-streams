package pl.bka.filters

import akka.NotUsed
import akka.stream.ThrottleMode
import akka.stream.scaladsl.{Flow, Source}
import pl.bka.{HeartBeat, Message}

import scala.concurrent.duration._

object HeartBeatMerge {
  def flow: Flow[Message, Message, NotUsed] = {
    val heartBeat = Source.fromIterator[HeartBeat](() => Iterator.continually(HeartBeat())).throttle(1, 1.seconds, 1, ThrottleMode.Shaping)
    Flow[Message].merge(heartBeat)
  }
}

