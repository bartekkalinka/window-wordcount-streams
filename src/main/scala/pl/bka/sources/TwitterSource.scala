package pl.bka.sources

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.auth.OAuth1
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Executors

import com.twitter.hbc.twitter4j.Twitter4jStatusClient
import com.google.common.collect.Lists
import com.twitter.hbc.twitter4j.handler.StatusStreamHandler
import com.twitter.hbc.twitter4j.message.{DisconnectMessage, StallWarningMessage}
import pl.bka.Config
import twitter4j.{StallWarning, Status, StatusDeletionNotice}

object TwitterSource {
  private val usersCounter = new scala.collection.concurrent.TrieMap[String, Int]

  private val listener = new StatusStreamHandler() {
    override def onStatus(status: Status) {
      val userName = status.getUser.getName
      val prevCount = usersCounter.getOrElse(userName, 0)
      if(prevCount >= 1) System.out.println(userName)
      usersCounter.put(userName, prevCount + 1)
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

  def run(config: Config) {
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
      client, queue, Lists.newArrayList(listener), service)

    t4jClient.connect()

    for(thread <- 0 to numProcessingThreads) {
      t4jClient.process()
    }

    Thread.sleep(50000)

    client.stop()

    System.out.println(s"The client read ${client.getStatsTracker.getNumMessages} messages!\n")
    System.out.println(s"Double entries: ${usersCounter.toSeq.filter(_._2 > 1)}")
  }
}

