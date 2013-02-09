
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure} 
import java.io.File
import scala.io.Source
 
object Main {
  def main(args: Array[String]) {    
    val promiseOfFinalResult = Promise[Seq[(String, Int)]]()
    val path = "src/main/resources/"

    val futureWithResult: Future[Seq[(String, Int)]] = for {
      files <- scanFiles(path)
      result <- processFiles(files)  
    } yield {
      result
    }

    futureWithResult.onSuccess { case r => promiseOfFinalResult.success(r) }

    promiseOfFinalResult.future.onComplete {
      case Success(result) => println(result) 
      case Failure(t) => t.printStackTrace
    }
  }

  private def processFiles(fileNames: Seq[String]): Future[Seq[(String, Int)]] = {
    val futures: Seq[Future[(String, Int)]] = fileNames.map(name => processFile(name))
    val singleFuture: Future[Seq[(String, Int)]] = Future.sequence(futures)
    singleFuture.map(r => r.sortWith(_._2 < _._2))
  }

  private def processFile(fileName: String): Future[(String, Int)] = 
  Future {
    val dataFile = new File(fileName)
    val wordCount = Source.fromFile(dataFile).getLines.foldRight(0)(_.split(" ").size + _)
    (fileName, wordCount)
  } recover {
    case e: java.io.IOException => 
      println("Something went wrong " + e)
      (fileName, 0)
  }

  private def scanFiles(docRoot: String):Future[Seq[String]] = Future { new File(docRoot).list.map(docRoot + _) }

}
