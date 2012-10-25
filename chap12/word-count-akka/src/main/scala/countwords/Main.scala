package countwords

import akka.actor._

object Main {
  class MainActor extends Actor {
    def receive = {
      case s: StartCounting =>
         val m = context.actorOf(Props[WordCountAccumulator])
         m ! s
      case FinishedCounting(result) =>
        println()
        println("final result " + result)
        println()
        context.system.shutdown()
    }
  }

  def main(args: Array[String]) = run

  private def run = {
	  val system = ActorSystem(name = "word-count")
    val m: ActorRef = system.actorOf(Props[MainActor], name = "main")
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 4)
  }
}
