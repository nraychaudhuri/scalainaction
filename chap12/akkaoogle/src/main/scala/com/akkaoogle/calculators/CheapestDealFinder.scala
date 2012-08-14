package com.akkaoogle.calculators

import messages._
import com.akkaoogle.db.models._
import akka.actor._
import akka.dispatch.Future
import akka.pattern.{ ask, pipe, AskTimeoutException }
import com.akkaoogle.infrastructure.RemoteActorServer

class CheapestDealFinder extends Actor {
  def receive = {
    case req: FindPrice =>
      val internalPrice =
        (RemoteActorServer.lookup("internal-load-balancer") ? req).mapTo[Option[LowestPrice]]
      val externalPrice =
        (RemoteActorServer.lookup("external-load-balancer") ? req).mapTo[Option[LowestPrice]]
      val lowestPrice: Future[Option[LowestPrice]] =
        findLowestPrice(internalPrice :: externalPrice :: Nil)
      lowestPrice pipeTo sender 
  }
}