// run with: scala ActorLinking.scala
// This you have to interrupt manually. Uncomment the 'exit...' line below and start again
// with: scala ActorLinking.scala
// the script will terminate.

import scala.actors.Actor._

val twoActor = actor {
  react {
    case _ => println("message handled")
  }  
}

val oneActor = actor {
  self.link(twoActor)
  react {
    case _ => 
      println("message handled")
      //now if you uncomment following line, both the actors will be terminated
      //exit(self, 'notNormal)
  }
}

oneActor ! 'Hello
Thread.sleep(300)
println("one actor " + oneActor.getState)
println("two actor " + twoActor.getState)

