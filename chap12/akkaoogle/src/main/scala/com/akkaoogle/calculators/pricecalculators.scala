package com.akkaoogle.calculators


object pricecalculators {
  import messages._
  import com.akkaoogle.db.models._
  import akka.actor._
  import akka.actor.newUuid
  import scala.io.Source
  import akka.dispatch.{Future, Futures}
  import com.akkaoogle.infrastructure.RemoteActorServer
  import com.akkaoogle.infrastructure.dispatchers._

  def findLowestPrice(futures: Iterable[Future[Nothing]]): Option[LowestPrice] = {
    val f: Future[Option[LowestPrice]] = Futures.fold(Option.empty[LowestPrice])(futures) {
      (lowestPrice: Option[LowestPrice], currentPrice: Option[LowestPrice]) =>
        currentPrice match {
          case Some(first) if (lowestPrice.isEmpty) => Some(first)
          case Some(c) if (c.price < lowestPrice.get.price) => Some(c)
          case _ => lowestPrice
        }
    }
    val lowestPrice: Option[LowestPrice] = f.await.result.flatMap(x => x)
    lowestPrice
  }


  class CheapestDealFinder extends Actor {
    def receive = {
      case req: FindPrice =>
        val internalPrice =
          RemoteActorServer.lookup("internal-load-balancer") !!! req
        val externalPrice =
          RemoteActorServer.lookup("external-load-balancer") !!! req
        val lowestPrice: Option[LowestPrice] =
          findLowestPrice(internalPrice :: externalPrice :: Nil)
        self.reply(lowestPrice)
    }
  }

  class ExternalPriceCalculator(val proxies: Iterable[ActorRef]) extends Actor {
    def receive = {
      case FindPrice(productId, quantity) =>
         val futures = proxies map { proxy =>
             proxy !!! FindPrice(productId, quantity)
         }
         val lowestPrice: Option[LowestPrice] = findLowestPrice(futures)
         val totalPrice: Option[LowestPrice] = lowestPrice.map { l =>
           l.copy(price = l.price  + (l.price * .02))
         }
         self.reply(totalPrice)
    }
  }

  class ExternalVendorProxyActor(val v:ExternalVendor) extends Actor {
    self.id = v.name
    self.timeout = 150L
    self.dispatcher = ExternalVendorProxyActor.dispatcher
    def receive = {
      case fp: FindPrice =>
        var result: Option[LowestPrice] = None
        val f = Future({
          val params = "?pd=" + fp.productDescription + "&q=" + fp.quantity
          val price = Source.fromURL(v.url + params).mkString.toDouble
          LowestPrice(v.name, fp.productDescription, price * fp.quantity)
        }, self.timeout)
        try {
          result = f.await.result
        } catch {
          case _ =>
            RemoteActorServer.lookup("monitor") ! LogTimeout(self.id, "Timeout for " + fp)
        }
        self.reply(result)
    }
  }

  class InternalPriceCalculator extends Actor {
    self.id = newUuid.toString
    self.dispatcher =  InternalPriceCalculatorActor.dispatcher

    def receive = {
      case FindPrice(productDescription, quantity) =>
        self.reply(calculatePrice(productDescription, quantity))
    }

    def calculatePrice(productDescription: String, qty: Int): Option[LowestPrice] = {
      Product.findByDescription(productDescription) map { product =>
        Some(LowestPrice(product.vendorName,
          product.description,
          product.calculatePrice * qty))
      } getOrElse None
    }
  }
}