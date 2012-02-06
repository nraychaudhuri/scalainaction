// run with <cmd_prompt>scala ReactWithPartialFunctionComposition.scala

import scala.actors.Actor._

def onCompletion = println("The actor has completed the job") 

def processMessages:Unit = react { case x: String => println("received " + x) }


val a = actor {
  { processMessages } andThen { onCompletion }
}

a ! "some message"

