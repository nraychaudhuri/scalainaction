package countwords

import akka.actor.Actor
import akka.actor.Actor._

object Main {
  class MainActor extends Actor {
    def receive = {
      case s: StartCounting =>
         val m = remote.actorFor("word-count-service", "localhost", 2552)
         m ! s
      case FinishedCounting(result) =>
        println()
        println("final result " + result)
        println()
        Actor.registry.shutdownAll()
        remote.shutdown
    }
  }

  def main(args: Array[String]) = run

  private def run = {
    remote.start("localhost", 2552)
    remote.register("word-count-service", actorOf[WordCountAccumulator])

    val m = Actor.actorOf[MainActor].start
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 2)
  }
}
