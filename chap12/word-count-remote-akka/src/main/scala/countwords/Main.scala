package countwords

import akka.actor._
import com.typesafe.config.ConfigFactory

object Main {
  class MainActor(accumulator: ActorRef) extends Actor {
    def receive = {
      case s: StartCounting =>
        accumulator ! s
      case FinishedCounting(result) =>
        println("result is processed by " + sender.path)
        println("final result " + result)
        context.system.shutdown()
    }
  }

  def main(args: Array[String]) = run

  private def run = {
    val mainSystem = ActorSystem("main")
    val accumulator = mainSystem.actorOf(Props[WordCountAccumulator], name ="accumulatorActor")
    val m = mainSystem.actorOf(Props(new MainActor(accumulator)))
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, 2)
  }
}