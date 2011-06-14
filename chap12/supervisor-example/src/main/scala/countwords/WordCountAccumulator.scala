package countwords

import scala.io._
import akka.actor.{ActorRef, Actor}
import akka.routing.Routing._
import akka.routing.SmallestMailboxFirstIterator
import java.net.URL

case class FileToCount(url:String) {
  def countWords = {
    Source.fromURL(new URL(url)).getLines.foldRight(0)(_.split(" ").size + _)
  }
}
case class WordCount(url:String, count: Int)
case class StartCounting(urls: List[String], workers: List[ActorRef])
case class FinishedCounting(result: List[(String, Int)])  

class WordCountWorker extends Actor {
  var lastMessage: Option[FileToCount] = None
  var lastSender: Option[ActorRef] = None

  override def preRestart(reason: Throwable) {
    //assuming that last reply is not send accumulator, so sending again
    lastSender.foreach { sender =>
      lastMessage.foreach ( m => sender ! WordCount(m.url, 0))
    }
  }

  override def postRestart(reason: Throwable) {
    lastMessage = None
    lastSender = None
  }

  def receive = {
    case x: FileToCount =>
      lastMessage = Some(x)
      lastSender = self.sender
      self.reply(WordCount(x.url, x.countWords))
  }
}

class WordCountAccumulator extends Actor {
  self.id = "word-count-service"
  private[this] var initiator: Option[ActorRef] = None
  private[this] var urlCount: Int = 0
  private[this] var sortedCount : List[(String, Int)] = Nil

  def receive = {
     case StartCounting(urls, workers) =>
       initiator = self.sender
       urlCount = urls.size
       beginSorting(urls, workers)
          
     case WordCount(url, count) =>
       sortedCount ::= (url, count)
       sortedCount = sortedCount.sortWith(_._2 < _._2)
       if(sortedCount.size == urlCount) {
         initiator.foreach {_ ! FinishedCounting(sortedCount) }
       }
  }
  
  private[this] def beginSorting(urls: List[String], workers: List[ActorRef]) {
    val balancer = loadBalancerActor(new SmallestMailboxFirstIterator(workers))
    urls.foreach( f => balancer ! FileToCount(f))
  }
}