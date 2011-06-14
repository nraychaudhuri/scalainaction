package actortest
import org.specs._
import scala.actors.Actor
import scala.actors.Actor._
import spy._
import SpyActor._

class SpyActorSpec extends Specification {
  
  "Spy actor" should {    
      val spyActor = SpyActor(500)
      val a = new EchoService().start
      val b = new DiscardService().start
    
      "enable synchronous assertion of specific messages" in {
          a ! "Hello" 
          spyActor.lastOne must be("Hello")
      }
            
      "enable assertion of multiple messages received in specific order" in {
          a ! "Hello"
          a ! "world"
          spyActor.last(2) must containAll(List("Hello", "world"))
      }
             
      "enable assertion for no kind of message" in {
          b ! "eat it"    
          spyActor.none must be(true)
      }       
  }
}

class EchoService extends Actor {
  override def act = {
    loop {
      receive {
        case msg => reply(msg)        
      }
    }
  }
}

class DiscardService extends Actor {
   override def act = {
     loop {
       receive {
         case msg =>         
       }
     }
   }
 }

