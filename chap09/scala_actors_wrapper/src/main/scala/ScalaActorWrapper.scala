import scala.actors._
import scala.actors.Actor._

package com.javaapi.forscala.actors {
  
  
  trait Effect[A] {
    def e(thisActor: Actor, msg: A): Unit
  }
  
  object ScalaActorWrapper {
	def actor[A](effect: Effect[A]) = {
		Actor.actor {
			loop {
				react {
					case Exit(from, reason) =>
						println("exit message received from " + from)
						Actor.exit
					case msg: A => effect.e(self, msg)
				}
			}
		}
	}
  }
}
