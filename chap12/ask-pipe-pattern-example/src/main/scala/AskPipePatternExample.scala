import akka.pattern.{ask, pipe}
import akka.actor._
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch._

object AskPipeExample extends App {
  
  implicit val timeout = Timeout(5 seconds) 
  
  class GreetingsActor extends Actor {
    val messageActor = context.actorOf(Props[GreetingsChildActor])
    def receive = {
      case name => 
        val f: Future[String] = (messageActor ask name).mapTo[String]
        f pipeTo sender 
    }
  }
  
  class GreetingsChildActor extends Actor {
    def receive = {
      case name => 
         val now = System.currentTimeMillis
         if(now % 2 == 0) 
            sender ! "Hey " + name  
        else 
            sender ! "Hello " + name
    }
  }

  val actorSystem = ActorSystem("askPipeSystem")
  
  val actor = actorSystem.actorOf(Props[GreetingsActor])
  
  val response: Future[String] = (actor ? "Nilanjan").mapTo[String]
  response.onComplete { e =>
    println(e)
    actorSystem.shutdown()
  }
  
}