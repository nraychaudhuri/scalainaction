package com.akkaoogle.monitor.specs

import org.specs2.mutable._
import com.akkaoogle.infrastructure.RemoteActorServer
import com.akkaoogle.calculators.messages.{Stats, FindStats, LogTimeout}
import org.h2.tools.Server
import java.io.{FileReader, BufferedReader, File}
import com.akkaoogle.db.models._
import com.akkaoogle.db.AkkaoogleSchema

class MonitorSpec extends Specification {
  val server = Server.createTcpServer().start()
  "Monitor actor" should {
    doBefore {
      AkkaoogleSchema.createSchema()
      RemoteActorServer.run()
    }

    doAfter {
      RemoteActorServer.stop()
    }

    "log timeouts" in {
      val monitor = RemoteActorServer.lookup("monitor")
      monitor ! LogTimeout("abc", "this is test timeout message")
      monitor ! LogTimeout("abc", "this is test timeout message")
      monitor ! LogTimeout("abc", "this is test timeout message")
      Thread.sleep(200)
      val resultOption = monitor !! FindStats("abc")
      resultOption.get must beEqualTo(Stats(actorId = "abc", timeouts = 3))
      TransactionFailure.findAll.size must beEqualTo(3)
    }

    doLast {
      server.stop()
      RemoteActorServer.stop()
    }

    def createNewLogFile() {
      val f = new File("src/test/resources/transaction.log")
      f.delete
      f.createNewFile
    }
  }
}