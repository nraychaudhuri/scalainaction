package countwords

import akka.actor._
import com.typesafe.config.ConfigFactory

object Main {
  class MainActor extends Actor {
    val workerSystem = ActorSystem("workersystem", ConfigFactory.load.getConfig("workersystem"))
    def receive = {
      case s: StartCounting =>
         val m = workerSystem.actorOf(Props[WordCountAccumulator])
         m ! s
      case FinishedCounting(result) =>
        println()
        println("final result " + result)
        println()
        context.system.shutdown()
				workerSystem.shutdown()
    }
  }

  def main(args: Array[String]) = run

  private def run = {
    val localSystem = ActorSystem("main")
    val m = localSystem.actorOf(Props[MainActor])
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 2)
  }
}