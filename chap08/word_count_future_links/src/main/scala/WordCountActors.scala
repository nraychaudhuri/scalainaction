import scala.actors._
import scala.actors.Actor._
import java.util.concurrent._
import java.io._
import scala.io._

case class FileToCount(fileName:String)
case class WordCount(fileName:String, count: Int)
case class StartCounting(docRoot: String, numActors: Int)
case class FinishedCounting(result: List[(String, Int)])  


class WordCountWorker(master: Actor) extends Actor {
  trapExit = true
  self.link(master)
  def countWords(fileName:String) = {
    val dataFile = new File(fileName)
    Source.fromFile(dataFile).getLines.foldRight(0)(_.split(" ").size + _)
  }
  
  def act {
    loop {
      react {
        case FileToCount(fileName:String) =>
          println("received file " + fileName + " for counting")
          val count = countWords(fileName)
          reply(WordCount(fileName, count))
        case Exit(from, reason) => Actor.exit
      }
    }
  }
}

class WordCountMaster extends Actor {
    
  def act {
    loop {
      react {
        case StartCounting(docRoot, numActors) => 
          val workers = createWorkers(numActors)
          val fileNames = scanFiles(docRoot) 
          val futures = beginSorting(fileNames, workers)
          val initiator = sender
          var i = 0
          var sortedCount : List[(String, Int)] = Nil 
          loopWhile(i < futures.size) {
            i += 1
            val input = futures(i - 1).inputChannel
            react {
              case input ! WordCount(fileName, count) => 
                sortedCount ::= (fileName, count)
                sortedCount = sortedCount.sortWith(_._2 < _._2)
                if(sortedCount.size == fileNames.size) {
                  initiator ! FinishedCounting(sortedCount)
                  Actor.exit
                }
              case Exit(from, UncaughtException(actor,Some(FileToCount(fileName)), _, _, cause)) =>
                println("Actor " + from + "terminated for " + cause)
                self.trapExit = true
                self.link(from)
                actor.restart
                sortedCount ::= (fileName, 0)
                sortedCount = sortedCount.sortWith(_._2 < _._2)
            }
          }
      }
    }
  }
  
  private def createWorkers(numActors: Int) = {
    val workers = for (i <- 0 until numActors) yield {
        val worker = new WordCountWorker(self).start
        self.trapExit = true
        self.link(worker)
        worker
      }
    workers.toList  
  }

  private def scanFiles(docRoot: String) = {
    val files = new File(docRoot).list.map(docRoot + _).toList
    docRoot + "unknown.txt" :: files
  }
  
  private[this] def beginSorting(fileNames: List[String], workers: List[Actor]) = {
    fileNames.zipWithIndex.map( e => {
      workers(e._2 % workers.size) !! FileToCount(e._1)
    })
  }  
}