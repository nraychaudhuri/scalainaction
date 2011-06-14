import scala.actors.Actor._
import scala.actors._

class UnstableActor extends Actor {
  def act = {
    loop {
      reactWithin(10000) {
        case 'SayHello => println("Hello there")
        case TIMEOUT => println("Timed out"); Actor.exit   
        case _ => throw new Exception("I don't know this message type")
      }
    }
  }
  
  override def exceptionHandler = {
    case e: Exception => println("Handled the exception. No worries")
  }
}

