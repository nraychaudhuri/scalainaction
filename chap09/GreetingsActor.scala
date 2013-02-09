object GreetingsActor extends App {

  import akka.actor.Props
  import akka.actor.ActorSystem
  import akka.actor.Actor

  case class Name(name: String)
  
  class GreetingsActor extends Actor {
    def receive = {
      case Name(n) => println("Hello " + n)
    }
  }

  val system = ActorSystem("greetings")
  val a = system.actorOf(Props[GreetingsActor], name = "greetings-actor")
 
  a ! Name("Nilanjan")

  Thread.sleep(50)
  system.shutdown() 
}
