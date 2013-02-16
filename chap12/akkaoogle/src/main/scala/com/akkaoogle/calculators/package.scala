package com.akkaoogle

import scala.concurrent.Future
import akka.util.Timeout
import akka.actor._
import akka.pattern.AskTimeoutException
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit
import akka.util.Timeout



package object calculators {
  import messages._
	
	implicit val timeout = Timeout(150, TimeUnit.MILLISECONDS)

  def findLowestPrice(futures: Iterable[Future[Option[LowestPrice]]]): Future[Option[LowestPrice]] = {
    Future.fold(futures)(Option.empty[LowestPrice]) {
      (lowestPrice: Option[LowestPrice], currentPrice: Option[LowestPrice]) => {
        currentPrice match {
          case Some(first) if (lowestPrice.isEmpty) => Some(first)
          case Some(c) if (c.price < lowestPrice.get.price) => Some(c)
          case _ => lowestPrice
        }
			}
    }
  }
}