package countwords

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import java.net.URL
import scala.io._

class WordCountMaster extends Actor {

  private[this] var urlCount: Int = _
  private[this] var sortedCount : Seq[(String, Int)] = Nil  
    
  def receive = {
    case StartCounting(urls, numActors) => 
      val workers = createWorkers(numActors)
      urlCount = urls.size
      beginSorting(urls, workers)
      
    case WordCount(url, count) => 
      println(s" ${url} -> ${count}")
      sortedCount = sortedCount :+ (url, count)
      sortedCount = sortedCount.sortWith(_._2 < _._2)
      if(sortedCount.size == urlCount) {
        println("final result " + sortedCount)
        finishSorting()
      }
  }

  override def postStop(): Unit = {
    println(s"Master actor is stopped: ${self}")
  }
  
  private def createWorkers(numActors: Int) = {
    for (i <- 0 until numActors) yield context.actorOf(Props[WordCountWorker], name = s"worker-${i}") 
  }

  private[this] def beginSorting(fileNames: Seq[String], workers: Seq[ActorRef]) {
    fileNames.zipWithIndex.foreach( e => {
      workers(e._2 % workers.size) ! FileToCount(e._1)
    })
  }
  
  private[this] def finishSorting() {
    context.system.shutdown()
  }
}