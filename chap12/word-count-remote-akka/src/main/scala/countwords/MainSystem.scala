package countwords

import akka.actor._
import com.typesafe.config.ConfigFactory
import scala.io.Source
import java.net.URL

case class FileToCount(url:String) {
  def countWords = {
    Source.fromURL(new URL(url)).getLines.foldRight(0)(_.split(" ").size + _)
  }
}
case class WordCount(url:String, count: Int)
case class StartCounting(urls: Seq[String], numActors: Int)

object MainSystem {
  class MainActor(accumulator: ActorRef) extends Actor {
    def receive = {
      case "start" =>
        val urls = List("http://www.infoq.com/",
          "http://www.dzone.com/links/index.html",
          "http://www.manning.com/",
          "http://www.reddit.com/")
        accumulator ! StartCounting(urls, 2)

    }
  }

  def main(args: Array[String]) = run

  private def run = {
    val mainSystem = ActorSystem("main", ConfigFactory.load.getConfig("mainsystem"))
    val accumulator = mainSystem.actorOf(Props[WordCountMaster], name ="wordCountMaster")
    val m = mainSystem.actorOf(Props(new MainActor(accumulator)))
    m ! "start"
  }
}