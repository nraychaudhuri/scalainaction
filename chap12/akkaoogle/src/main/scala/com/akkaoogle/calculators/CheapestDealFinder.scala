package com.akkaoogle.calculators

import messages._
import com.akkaoogle.db.models._
import akka.actor._
import scala.concurrent.Future
import akka.pattern.{ ask, pipe, AskTimeoutException }
import com.akkaoogle.infrastructure.AkkaoogleActorServer
import scala.concurrent.ExecutionContext.Implicits.global

class CheapestDealFinder extends Actor {
  def receive = {
    case req: FindPrice =>
      val internalPrice =
        (AkkaoogleActorServer.lookup("internal-load-balancer") ? req).mapTo[Option[LowestPrice]]
      val externalPrice =
        (AkkaoogleActorServer.lookup("external-load-balancer") ? req).mapTo[Option[LowestPrice]] recover {
				case e: AskTimeoutException => Option.empty[LowestPrice]
      }
      val lowestPrice: Future[Option[LowestPrice]] =
        findLowestPrice(Seq(internalPrice, externalPrice))
      lowestPrice pipeTo sender 
  }
}