import java.util.concurrent._
import java.io._
import scala.io._
import scala.collection.immutable.Stack

class WordCountWorker(master: WordCountMaster) extends Thread {
  private var requests = Stack[String]()
  private var done= false
  
  private def countWords = synchronized {
    val fileName = requests.head
    val dataFile = new File(fileName)
    val count = Source.fromFile(dataFile).getLines.foldRight(0)(_.split(" ").size + _)
    master.addCount(fileName, count)
    requests = requests.tail
    notifyAll()
  }
  
  def addRequest(fileName: String) { requests = requests :+ fileName }
  def die = { done = true }
  
  override def run() = synchronized {
    while(!done) {
      if(!requests.isEmpty) { countWords }
      wait(10)
    }
  }
}

class WordCountMaster(docRoot: String, numWorkers: Int) extends Thread {
  var sortedCount : List[(String,Int)] = Nil
  
  private var latch: CountDownLatch = null
  private var workers: List[WordCountWorker] = Nil
  private var done = false
  private var responses: List[(String, Int)] = Nil
  
  def addCount(fileName: String, count: Int) = synchronized { responses ::= (fileName, count) }
  
  private[this] def assignWork(index: Int, fileName: String) = synchronized {
    val nextWorker = workers(index)
    nextWorker.addRequest(docRoot + fileName)
    println("assigning new work to " + nextWorker.getName)
    notifyAll()        
  }
  
  def beginCounting {
    start()
    workers = (for (i <- 0 until numWorkers) 
                  yield createWorkerThread).toList
    val dir = new File(docRoot)
    var cnt = 0
    dir.list.foreach(file => {
      assignWork(cnt % numWorkers, file)
      cnt += 1
    })
    latch = new CountDownLatch(cnt)
  }
  
  private[this] def finish {
    workers.foreach { _.die }
    done = true
  }
  
  def waitUntilDone = {
    latch.await
    finish
  }
  
  override def run() = synchronized {
    while(!done) {
      if(!responses.isEmpty) { 
        sortedCount ::= responses.head
        sortedCount = sortedCount.sortWith(_._2 < _._2)
        responses = responses.tail
        latch.countDown
      }
      wait(10)
    }
  }
  
  private[this] def createWorkerThread = {
    val worker = new WordCountWorker(this)
    worker.start()
    worker
  }
}
