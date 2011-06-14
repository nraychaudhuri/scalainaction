def throwableToLeft[T](block: => T): Either[java.lang.Throwable, T] =
  try {
    Right(block)
  } catch {
    case ex => Left(ex)
  }
  
import java.net._

throwableToLeft { new Socket("localhost", 4444) } match {
  case Right(s) => println(s)
  case Left(t) => t.printStackTrace
}