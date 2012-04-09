import scala.actors._
import scala.actors.Actor._
import scala.actors.remote.RemoteActor
import scala.actors.remote.Node
import java.util.Date

abstract class Remotely(name: Symbol, port: Int) {
  actor {
    RemoteActor.alive(port)
    RemoteActor.register(name, self)
    loop {
      react(handler)
    }    
  }
  def handler: PartialFunction[Any, Unit]
}

class AuditingSystem(port: Int) extends Remotely('Auditing, port) {  
  override def handler = {
    case e : Any =>
      println("updating the audits")
  }
}

class ReportingSystem(port: Int) extends Remotely('Reporting, port) {  
  override def handler = {
        case i : Any =>
          println("updating the reporting system")
  }
}

object EventPublisher {
  def start(port: Int = 9999, subscribers: Map[Symbol, Node]) {
    val actors = subscribers map { p => RemoteActor.select(p._2, p._1) }
    new EventPublisher(port, actors.toList)
  }
}

class EventPublisher(port: Int, subscribers: List[AbstractActor]) 
        extends Remotely('EventTracker, port) {
  override def handler = {
     case e: Any => 
          println("publishing event " + e)
          publishEvent(e)
  }
  private def publishEvent(e: Any) {
    subscribers foreach {_ ! e }
  }
}



