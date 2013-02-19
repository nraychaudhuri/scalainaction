import akka.agent.Agent
import akka.actor.ActorSystem
import java.io.{BufferedReader, FileReader, FileWriter}
import java.util.concurrent.CountDownLatch
import akka.util.Timeout
import org.specs2.mutable.Specification

class AgentExampleSpecs extends Specification {

  "AgentExample" should {
    "write to the log file asynchronously" in {
	    implicit val system = ActorSystem("agentExample") 
			implicit val timeout = new Timeout(100, java.util.concurrent.TimeUnit.MILLISECONDS)
      val writer = new FileWriter("src/test/resources/log.txt")
      val a = Agent(writer)
      a.send { w => w.write("This is a log message"); w}
      a.await
      a.close
      writer.close
      system.shutdown() 
      val l = new BufferedReader(new FileReader("src/test/resources/log.txt")).readLine
      l must be_==("This is a log message")
    }
  }
}
