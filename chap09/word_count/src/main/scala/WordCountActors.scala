
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import java.io._
import scala.io._

case class FileToCount(fileName:String)
case class WordCount(fileName:String, count: Int)
case class StartCounting(docRoot: String, numActors: Int)


class WordCountWorker extends Actor {
  def countWords(fileName:String) = {
    val dataFile = new File(fileName)
    Source.fromFile(dataFile).getLines.foldRight(0)(_.split(" ").size + _)
  }
  
  def receive = {
    case FileToCount(fileName:String) =>
      val count = countWords(fileName)
      sender ! WordCount(fileName, count)
  }

  override def postStop(): Unit = {
    println(s"Worker actor is stopped: ${self}")
  }

}

class WordCountMaster extends Actor {

  var fileNames: Seq[String] = Nil
  var sortedCount : Seq[(String, Int)] = Nil  
    
  def receive = {
    case StartCounting(docRoot, numActors) => 
      val workers = createWorkers(numActors)
      fileNames = scanFiles(docRoot) 
      beginSorting(fileNames, workers)
      
    case WordCount(fileName, count) => 
      sortedCount = sortedCount :+ (fileName, count)
      sortedCount = sortedCount.sortWith(_._2 < _._2)
      if(sortedCount.size == fileNames.size) {
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

  private def scanFiles(docRoot: String) = 
    new File(docRoot).list.map(docRoot + _)
  
  private[this] def beginSorting(fileNames: Seq[String], workers: Seq[ActorRef]) {
    fileNames.zipWithIndex.foreach( e => {
      workers(e._2 % workers.size) ! FileToCount(e._1)
    })
  }
  
  private[this] def finishSorting() {
    context.system.shutdown()
  }
}