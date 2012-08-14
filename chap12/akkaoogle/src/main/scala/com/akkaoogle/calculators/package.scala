package com.akkaoogle

import akka.dispatch.{ Future, Futures }
import akka.util.duration._
import akka.util.Timeout
import akka.actor._
import akka.dispatch.ExecutionContext

package object calculators {
  import messages._
	
	implicit val timeout = Timeout(150 milliseconds)
  implicit val system = ActorSystem("calculators")
  implicit val ec = ExecutionContext.defaultExecutionContext

  def findLowestPrice(futures: Iterable[Future[Option[LowestPrice]]]): Future[Option[LowestPrice]] = {
    val f: Future[Option[LowestPrice]] = Future.fold(futures)(Option.empty[LowestPrice]) {
      (lowestPrice: Option[LowestPrice], currentPrice: Option[LowestPrice]) => {
        currentPrice match {
          case Some(first) if (lowestPrice.isEmpty) => Some(first)
          case Some(c) if (c.price < lowestPrice.get.price) => Some(c)
          case _ => lowestPrice
        }
			}
    }
    f
  }

  
}