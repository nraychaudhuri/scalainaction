package com.akkaoogle.infrastructure
import akka.agent.Agent
import akka.actor.Actor
import com.akkaoogle.calculators.messages.{Stats, FindStats, LogTimeout}
import java.util.Date
import com.akkaoogle.db.models._

class MonitorActor extends Actor {
  import context._
  val errorLogger = Agent(Map.empty[String, Int])

  def preRestart = errorLogger send { old => Map.empty[String, Int] }

  def receive = {
    case LogTimeout(actorId, msg) =>
      logTimeout(actorId, msg)
    case FindStats(actorId) =>
      val timeouts = errorLogger().getOrElse(actorId, 0)
      sender ! Stats(actorId, timeouts = timeouts) 
  }

  private def logTimeout(actorId: String, msg: String): Unit = {
    errorLogger send { errorLog =>
	    val current = errorLog.getOrElse(actorId, 0)
      val newErrorLog =  errorLog + (actorId -> (current + 1))
      val l = new TransactionFailure(actorId, msg, new Date(System.currentTimeMillis))
      l.save
      newErrorLog
    }
  }
}
