package countwords

import java.io._
import scala.io._
import akka.actor._
import akka.routing._
import java.net.URL
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.SupervisorStrategy._

case class FileToCount(url:String) {
  def countWords = {
    Source.fromURL(new URL(url)).getLines.foldRight(0)(_.split(" ").size + _)
  }
}
case class WordCount(url:String, count: Int)
case class StartCounting(urls: List[String], numActors: Int)
case class FinishedCounting(result: List[(String, Int)])  


class WordCountWorker extends Actor {
  var lastSender: Option[ActorRef] = None


  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {
	  case _: Exception => Restart
	}

  override def preRestart(reason: Throwable, lastMessage: Option[Any]) {
    //assuming that last reply is not send accumulator, so sending again
    for {
	    sender <- lastSender
	    message <- lastMessage
    } {
	    message match { case e: FileToCount => sender ! WordCount(e.url, 0)}
    }
  }

  override def postRestart(reason: Throwable) {
    lastSender = None
  }

  def receive = {
    case x: FileToCount =>
      //lastMessage = Some(x)
      lastSender = Some(sender)
      sender ! WordCount(x.url, x.countWords)
  }
}

class WordCountAccumulator extends Actor {

  private[this] var initiator: Option[ActorRef] = None
  private[this] var urlCount: Int = 0
  private[this] var sortedCount : List[(String, Int)] = Nil


  override val supervisorStrategy = AllForOneStrategy() {
	  case _: Exception => 
	    println("Restarting...")
			Restart
	}

  def receive = {
     case StartCounting(urls, numActors) =>
       initiator = Some(sender)
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
    (for (i <- 0 until numActors) yield context.actorOf(Props[WordCountWorker])).toList

  private def scanFiles(docRoot: String) = 
    new File(docRoot).list.map(docRoot + _).toList
  
  private[this] def beginSorting(fileNames: List[String], workers: List[ActorRef]) {
    //chapter 12 explains all the Akka routers
	  val balancer = context.actorOf(
							Props[WordCountWorker].withRouter(SmallestMailboxRouter(routees = workers)), 
							name = "balancer")     
    fileNames.foreach( f => balancer ! FileToCount(f))
  }
}