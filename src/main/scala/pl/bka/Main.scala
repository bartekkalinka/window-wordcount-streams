package pl.bka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import pl.bka.displays.{PrintlnDisplay, WebsocketDisplay}
import pl.bka.filters.Distinct
import pl.bka.soruces.TextFileSource
import pl.bka.sources.TwitterSource
import pl.bka.windows.Top

import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val source =
      args(0) match {
        case "text" =>
          TextFileSource.words("input3.txt", 1.millis)
            .via(Top.nwords(500, 4, 4))
            .via(Distinct.distinct((0, "")))
        case "twitter" =>
          TwitterSource.source(Config(ConfigFactory.load()))
            .via(Top.wordWithOccurence(5000, 3))
            .via(Distinct.distinct((0, "")))
      }
    args(1) match {
      case "web" =>
        WebsocketDisplay(source.map(_.toString)).bind()
      case "stdout" =>
        PrintlnDisplay(source).display()
    }
  }
}

