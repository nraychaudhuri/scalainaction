package countwords

import java.io._
import scala.io._
import akka.actor.{ActorRef, Actor}
import akka.routing.Routing._
import akka.routing.{SmallestMailboxFirstIterator, CyclicIterator}
import java.net.URL

case class FileToCount(url:String) {
  def countWords = {
    Source.fromURL(new URL(url)).getLines.foldRight(0)(_.split(" ").size + _)
  }
}
case class WordCount(url:String, count: Int)
case class StartCounting(urls: List[String], numActors: Int)
case class FinishedCounting(result: List[(String, Int)])  

class WordCountAccumulator extends Actor {

  private[this] var initiator: Option[ActorRef] = None
  private[this] var urlCount: Int = 0
  private[this] var sortedCount : List[(String, Int)] = Nil

  def receive = {
     case StartCounting(urls, numActors) =>
       initiator = self.sender
       urlCount = urls.size
       beginSorting(urls, createWorkers(numActors))
          
     case WordCount(url, count) =>
       sortedCount ::= (url, count)
       sortedCount = sortedCount.sortWith(_._2 < _._2)
       if(sortedCount.size == urlCount) {
         initiator.foreach {_ ! FinishedCounting(sortedCount) }
       }
  }
  
  private def createWorkers(numActors: Int) =
    (for (i <- 0 until numActors)
        yield self.spawnLinkRemote[WordCountWorker]("localhost", 2552, timeout = 500L)).toList

  private[this] def beginSorting(urls: List[String], workers: List[ActorRef]) {
    val balancer = loadBalancerActor(new SmallestMailboxFirstIterator(workers))
    urls.foreach( f => balancer ! FileToCount(f))
  }
}