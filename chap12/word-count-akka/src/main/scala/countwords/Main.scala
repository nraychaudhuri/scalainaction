package countwords

import akka.actor.Actor

object Main {
  class MainActor extends Actor {
    def receive = {
      case s: StartCounting =>
         val m = Actor.actorOf[WordCountAccumulator].start
         m ! s
      case FinishedCounting(result) =>
        println()
        println("final result " + result)
        println()
        Actor.registry.shutdownAll()
    }
  }

  def main(args: Array[String]) = run

  private def run = {
    val m = Actor.actorOf[MainActor].start
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 2)
  }
}
