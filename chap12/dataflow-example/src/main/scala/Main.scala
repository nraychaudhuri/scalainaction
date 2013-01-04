import akka.actor._
import akka.dispatch._
import Future.flow

object Main extends App {
	implicit val system = ActorSystem("dataflow")
	val messageFromFuture, rawMessage, parsedMessage = Promise[String]()
	flow {
		messageFromFuture << parsedMessage()
		println("z = " + messageFromFuture())
	}
	flow { rawMessage << "olleh" }
	flow { parsedMessage << toAscii(rawMessage()) }
	
	
	def toAscii(s: String) = s.reverse
}