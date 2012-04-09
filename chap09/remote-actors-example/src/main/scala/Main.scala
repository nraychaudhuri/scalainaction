
import scala.actors.remote._
import scala.actors.Actor._
import scala.actors._

object Main{
  def main(args : Array[String]){
    new AuditingSystem(9911)
    new ReportingSystem(9922)
    EventPublisher.start(9933, Map('Auditing -> Node("127.0.0.1", 9911), 'Reporting -> Node("127.0.0.1", 9922)))
    
    val tracker = RemoteActor.select(Node("127.0.0.1", 9933), 'EventTracker)
    tracker ! "a event"
  }
}
