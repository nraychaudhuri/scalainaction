package countwords

import akka.actor.Actor._
import akka.actor.{Supervisor, Actor}
import akka.config.Supervision._

object Main {
  class MainActor extends Actor {
    def receive = {
      case s: StartCounting =>
         val m = remote.actorFor("word-count-service", "localhost", 2552)
         m ! s
      case FinishedCounting(result) =>
        println()
        println("final result " + result)
        println()
        Actor.registry.shutdownAll()
        remote.shutdown
    }
  }

  def main(args: Array[String]) = run

  private def createWorkers(numActors: Int) =
    (for (i <- 0 until numActors)
        yield remote.actorOf[WordCountWorker]("localhost", 2552)).toList

  private def run = {
    remote.start("localhost", 2552)

    val workers = createWorkers(2)
    val subordinates = for(w <- workers) yield Supervise(w, Permanent, true)
    val workerSupervisorConfig = SupervisorConfig(
        OneForOneStrategy(List(classOf[Exception]), 3, 1000),
        subordinates
      )

    val supervisor = Supervisor(
      SupervisorConfig(
        AllForOneStrategy(List(classOf[Exception]), 3, 1000),
        Supervise(
          actorOf[WordCountAccumulator],
          Permanent,
          true)
        :: workerSupervisorConfig :: Nil))
    val m = Actor.actorOf[MainActor].start
    val urls = List("http://www.infoq.com/",
      "http://www.dzone.com/links/index.html",
      "http://www.manning.com/",
      "http://www.jgldfkjgl.com/",
      "http://www.kjsgkfs.com/",
      "http://www.eryteryhfgh.com/",
      "http://www.dfhdhdh.com/",
      "http://www.utlmnhkhktdb.com/",
      "http://www.javaworld.com/",
      "http://www.reddit.com/")
    m ! StartCounting(urls, workers)
  }
}
