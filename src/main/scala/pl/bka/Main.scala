package pl.bka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import pl.bka.displays.{PrintlnDisplay, WebsocketDisplay}
import pl.bka.filters.{Distinct, WarmUpWindow}
import pl.bka.soruces.TextFileSource
import pl.bka.sources.TwitterSource
import pl.bka.windows.Top

import scala.concurrent.duration._
import spray.json._
import DefaultJsonProtocol._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val windowSize = if(args.length >= 3) args(2).toInt else 5000
    def minWordLength(default: Int) = if(args.length >= 4) args(3).toInt else default
    val source =
      args(0) match {
        case "text" =>
          WarmUpWindow.fakeWords(windowSize)
            .concat(TextFileSource.words("input3.txt", 100.nanos))
            .via(Top.nwordsSliding(windowSize, 6, minWordLength(5)))
            .via(Distinct.distinct(Seq((0, ""))))
        case "twitter" =>
          WarmUpWindow.fakeWords(windowSize)
            .concat(TwitterSource.source(Config(ConfigFactory.load())))
            .via(Top.nwordsSliding(windowSize, 6, minWordLength(1)))
            .via(Distinct.distinct(Seq((0, ""))))
      }
    args(1) match {
      case "web" =>
        WebsocketDisplay(source.map(_.toJson.toString)).bind()
      case "stdout" =>
        PrintlnDisplay(source.map(_.toJson.toString)).display()
    }
  }
}

