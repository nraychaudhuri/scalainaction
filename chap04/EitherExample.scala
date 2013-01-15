// check in windows with >netstat -a
// or in Unix with >lsof | grep TCP 
// the used ports on your machine and enter one of them as port(val port = ...)
// Start the scala REPL and load EitherExample.scala >:load EitherExample.scala 
package chap04

object EitherExample extends App {
	val port = 4444

	def throwableToLeft[T](block: => T): Either[java.lang.Throwable, T] =
	  try {
	    Right(block)
	  } catch {
	    case ex: Throwable => Left(ex)
	  }

	import java.net._

	throwableToLeft { new Socket("localhost", port) } match {
	  case Right(s) => println(s)
	  case Left(t) => t.printStackTrace
	}	
}

