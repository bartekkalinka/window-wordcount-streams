package pl.bka.sources

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.auth.OAuth1
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Executors

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{Materializer, OverflowStrategy}
import akka.stream.scaladsl.{Sink, Source}
import com.twitter.hbc.twitter4j.Twitter4jStatusClient
import com.google.common.collect.Lists
import com.twitter.hbc.twitter4j.handler.StatusStreamHandler
import com.twitter.hbc.twitter4j.message.{DisconnectMessage, StallWarningMessage}
import org.reactivestreams.Publisher
import pl.bka.Config
import twitter4j.{StallWarning, Status, StatusDeletionNotice}

import scala.concurrent.Future

object TwitterSource {
  def source(config: Config)(implicit fm: Materializer, system: ActorSystem): Source[String, NotUsed] = {
    import system.dispatcher
    val (streamEntry: ActorRef, publisher: Publisher[String]) =
      Source.actorRef[String](1000, OverflowStrategy.dropHead).toMat(Sink.asPublisher(fanout = false))((a, b) => (a, b)).run
    Future(runTwitterClient(config, streamEntry))
    Source.fromPublisher(publisher)
  }

  private def runTwitterClient(config: Config, streamEntry: ActorRef) {
    val (consumerKey, consumerSecret, token, secret) = config.twitterConfig
    val queue = new LinkedBlockingQueue[String](10000)

    val endpoint = new StatusesSampleEndpoint()
    endpoint.stallWarnings(false)

    val auth = new OAuth1(consumerKey, consumerSecret, token, secret)

    val client = new ClientBuilder()
      .name("sampleExampleClient")
      .hosts(Constants.STREAM_HOST)
      .endpoint(endpoint)
      .authentication(auth)
      .processor(new StringDelimitedProcessor(queue))
      .build()

    val numProcessingThreads = 4
    val service = Executors.newFixedThreadPool(numProcessingThreads)

    val t4jClient = new Twitter4jStatusClient(
      client, queue, Lists.newArrayList(listener(streamEntry)), service)

    t4jClient.connect()

    for(thread <- 0 to numProcessingThreads) {
      t4jClient.process()
    }
  }

  private def listener(streamEntry: ActorRef) = new StatusStreamHandler() {
    override def onStatus(status: Status) {
      val userName = status.getUser.getName
      streamEntry ! userName
    }
    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) = {}
    override def onTrackLimitationNotice(limit: Int) = {}
    override def onScrubGeo(user: Long, upToStatus: Long) = {}
    override def onStallWarning(warning: StallWarning) = {}
    override def onException(e: Exception) = {}
    override def onDisconnectMessage(message: DisconnectMessage) = {}
    override def onStallWarningMessage(warning: StallWarningMessage) = {}
    override def onUnknownMessageType(s: String) = {}
  }
}

