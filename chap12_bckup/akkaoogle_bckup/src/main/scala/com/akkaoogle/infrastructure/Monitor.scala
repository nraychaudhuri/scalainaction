package com.akkaoogle.infrastructure
import akka.stm._
import akka.agent.Agent
import akka.actor.Actor
import com.akkaoogle.calculators.messages.{Stats, FindStats, LogTimeout}
import java.util.Date
import com.akkaoogle.db.models._

class MonitorActor extends Actor {
  self.id = "monitor"
  val errorLog = TransactionalMap[String, Int]
  val dbLogger = Agent[TransactionFailure](null)

  def preRestart = {
    errorLog.clear
  }

  def receive = {
    case LogTimeout(actorId, msg) =>
      logTimeout(actorId, msg)
    case FindStats(actorId) =>
      val timeouts = errorLog.get(actorId).getOrElse(0)
      self.reply(Stats(actorId, timeouts = timeouts))
  }

  private def logTimeout(actorId: String, msg: String): Unit = atomic {
    val current = errorLog.get(actorId).getOrElse(0)
    errorLog += (actorId -> (current + 1))
    dbLogger send {
      l =>
        val l = new TransactionFailure(actorId, msg, new Date(System.currentTimeMillis))
        l.save
        l
    }
  }
}
