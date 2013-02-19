import akka.dataflow._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise


object Main extends App {
	val messageFromFuture, rawMessage, parsedMessage = Promise[String]()
	flow {
		messageFromFuture << parsedMessage()
		println("z = " + messageFromFuture())
	}
	flow { rawMessage << "olleh" }
	flow { parsedMessage << toAscii(rawMessage()) }
	
	
	def toAscii(s: String) = s.reverse
}