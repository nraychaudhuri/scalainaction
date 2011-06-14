import scala.actors._
import scala.actors.Actor._
import java.util.concurrent._
import java.io._
import scala.io._

case class FileToCount(fileName:String)
case class WordCount(fileName:String, count: Int)
case class StartCounting(docRoot: String, numActors: Int)
case class FinishedCounting(result: List[(String, Int)])  


class WordCountWorker extends Actor {
  def countWords(fileName:String) = {
    val dataFile = new File(fileName)
    Source.fromFile(dataFile).getLines.foldRight(0)(_.split(" ").size + _)
  }
  def act {
    loop {
      receive {
        case FileToCount(fileName:String) =>
          val count = countWords(fileName)
          reply(WordCount(fileName, count))
        case Exit(from, reason) => Actor.exit
      }
    }
  }
}

class WordCountMaster extends Actor {
    
  def act {
    var initiator: Option[OutputChannel[Any]] = None
    var workers: List[Actor] = Nil
    var fileNames: List[String] = Nil
    var sortedCount : List[(String, Int)] = Nil  
    
    loop {
      receive {
        case StartCounting(docRoot, numActors) => 
          initiator = Some(sender)
          workers = createWorkers(numActors)
          fileNames = scanFiles(docRoot) 
          beginSorting(fileNames, workers)
          
        case WordCount(fileName, count) => 
          sortedCount ::= (fileName, count)
          sortedCount = sortedCount.sortWith(_._2 < _._2)
          if(sortedCount.size == fileNames.size) {
            initiator.foreach {_ ! FinishedCounting(sortedCount) }
            finishSorting(workers)
          }
      }
    }
  }
  
  private def createWorkers(numActors: Int) = 
    (for (i <- 0 until numActors) yield (new WordCountWorker).start).toList 

  private def scanFiles(docRoot: String) = 
    new File(docRoot).list.map(docRoot + _).toList
  
  private[this] def beginSorting(fileNames: List[String], workers: List[Actor]) {
    fileNames.zipWithIndex.foreach( e => {
      workers(e._2 % workers.size) ! FileToCount(e._1)
    })
  }
  
  private[this] def finishSorting(workers: List[Actor]) {
    workers foreach { _ ! Exit(this, "Done with the work") }
    Actor.exit
  }
}