// start scala REPL and 
// :load HandlingActorReplies.scala

import scala.actors.Actor._

val echoActor = actor {
  loop {
    react {
      case msg: String => reply(msg) 
    }
  }
}

actor {
  echoActor ! "Knock Knock"
  receive {
    case response: String => println("Handling response asynchronously " + response)
  }
}

echoActor !? "Knock Knock" match {
  case response: String => println("Handling response synchronously " + response)
}

val future = echoActor !! "Knock Knock"
//do something else
//now ready to handle response
println("Handling response using future " + future())



