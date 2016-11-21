package pl.bka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import pl.bka.displays.{PrintlnDisplay, WebsocketDisplay}
import pl.bka.soruces.TextFileSource
import pl.bka.sources.TwitterSource
import pl.bka.windows.Top

import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val source = TextFileSource.words("input3.txt", 10.millis)
      .via(Top.wordWithOccurence(50, 3))
      .via(Distinct.distinct((0, "")))
    args(0) match {
      case "web" =>
        WebsocketDisplay(source.map(_.toString)).bind()
      case "stdout" =>
        PrintlnDisplay(source).display()
      case "twitter" =>
        val twitterSource = TwitterSource.source(Config(ConfigFactory.load()))
        PrintlnDisplay(twitterSource).display()
    }
  }
}

