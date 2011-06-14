import scala.actors.Actor._

def onCompletion {println("The actor has completed the job") }

def processMessages { react { case x: String => println("received some message") }

val a = actor {
  { processMessages } andThen {onCompletion }
}

a ! "some message"

