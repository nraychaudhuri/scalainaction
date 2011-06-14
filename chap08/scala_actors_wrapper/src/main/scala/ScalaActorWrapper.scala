import scala.actors._
import scala.actors.Actor._

package com.javaapi.forscala.actors {
  
  
  trait Effect[A] {
    def e(thisActor: Actor, msg: A): Unit
  }

}
