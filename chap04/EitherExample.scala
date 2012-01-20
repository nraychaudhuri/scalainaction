// check in windows with >netstat -a
// or in Unix with >lsof | grep TCP 
// the used ports on your machine and enter one of them as port(val port = ...)
// Start scala und load EitherExample.scala >:load EitherExample.scala 


val port = 5354

def throwableToLeft[T](block: => T): Either[java.lang.Throwable, T] =
  try {
    Right(block)
  } catch {
    case ex => Left(ex)
  }
  
import java.net._

throwableToLeft { new Socket("localhost", port) } match {
  case Right(s) => println(s)
  case Left(t) => t.printStackTrace
}