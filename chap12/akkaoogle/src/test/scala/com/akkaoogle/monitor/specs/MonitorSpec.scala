package com.akkaoogle.monitor.specs

import org.specs2.mutable._
import com.akkaoogle.infrastructure.AkkaoogleActorServer
import com.akkaoogle.calculators.messages.{Stats, FindStats, LogTimeout}
import java.io.{FileReader, BufferedReader, File}
import com.akkaoogle.db.models._
import com.akkaoogle.db.AkkaoogleSchema
import akka.pattern.{ask, pipe }
import scala.concurrent.Await
import akka.util.Timeout
import com.akkaoogle.helpers._

class MonitorSpec extends Specification {
	implicit val timeout = new Timeout(100, java.util.concurrent.TimeUnit.MILLISECONDS)

  step {
	  H2Server.start()
	  AkkaoogleSchema.createSchema()
	  AkkaoogleActorServer.run()	
  } 
	
  "Monitor actor" should {
    "log timeouts" in {
      val monitor = AkkaoogleActorServer.lookup("monitor")
      monitor ! LogTimeout("abc", "this is test timeout message")
      monitor ! LogTimeout("abc", "this is test timeout message")
      monitor ! LogTimeout("abc", "this is test timeout message")
      Thread.sleep(1000)
      val future = monitor ? FindStats("abc")
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Stats(actorId = "abc", timeouts = 3))
      TransactionFailure.findAll.size must beEqualTo(3)
    }
  }

  step {
    AkkaoogleActorServer.stop()
	  H2Server.stop()
  }
}