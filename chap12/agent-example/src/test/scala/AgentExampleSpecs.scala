import akka.agent.Agent
import java.io.{BufferedReader, FileReader, FileWriter}
import java.util.concurrent.CountDownLatch
import org.specs2._
import org.specs2.mutable.Specification

class AgentExampleSpecs extends Specification {

  "AgentExample" should {
    "write to the log file asynchronously" in {
      val writer = new FileWriter("src/test/resources/log.txt")
      val a = Agent(writer)
      val latch = new CountDownLatch(1)
      a.send { w => w.write("This is a log message"); w}
      a.send {w => latch.countDown; w }
      latch.await
      a.close
      writer.close
      val l = new BufferedReader(new FileReader("src/test/resources/log.txt")).readLine
      l must be_==("This is a log message")
    }
  }
}
