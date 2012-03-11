// Start scala >scala
// load into the REPL> :load Maybe.scala
package chap04

object MaybeExample {
	sealed abstract class Maybe[+A] {
	  def isEmpty: Boolean
	  def get: A  
	  def getOrElse[B >: A](default: B): B = {
	    if(isEmpty) default else get
	  }
	}

	final case class Just[A](value: A) extends Maybe[A] {
	  def isEmpty = false
	  def get = value
	}

	case object Nil extends Maybe[scala.Nothing] {
	  def isEmpty = true
	  def get = throw new NoSuchElementException("Nil.get")
	}	
}

