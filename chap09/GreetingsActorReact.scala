// run with <cmd_prompt>scala GreetingsActorReact.scala

case class Name(name: String)

class GreetingsActor extends scala.actors.Actor {
  def act = {
    react {
      case Name(n) => println("Hello " + n)
    }
    println("I guess I am done greeting people")
  }
}

val a = new GreetingsActor
a.start
a ! Name("Nilanjan")