package countwords

import akka.actor._
import com.typesafe.config.ConfigFactory

object Main {
  class MainActor extends Actor {
    def receive = {
      case s: StartCounting =>
         val m = context.actorOf(Props[WordCountAccumulator], name = "accumulatorActor")
         m ! s
      case FinishedCounting(result) =>
        println("result is processed by " + sender.path)
        println()
        println("final result " + result)
        println()
        context.system.shutdown()
    }
  }

  def main(args: Array[String]) = run

  private def run = {
    val localSystem = ActorSystem("main", ConfigFactory.load.getConfig("mainsystem"))
    val m = localSystem.actorOf(Props[MainActor])
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 2)
  }
}