package com.akkaoogle.calculators
import messages._
import com.akkaoogle.db.models._
import akka.actor._
import scala.io.Source
import akka.pattern.{ ask, pipe, AskTimeoutException }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.AskTimeoutException
import com.akkaoogle.infrastructure.AkkaoogleActorServer


class ExternalPriceCalculator(val proxies: Iterable[ActorRef]) extends Actor {
    def receive = {
      case FindPrice(productId, quantity) =>
        val futures = proxies map { proxy =>
	        val fp = FindPrice(productId, quantity)
          (proxy ? fp).mapTo[Option[LowestPrice]] recover {
            case e: AskTimeoutException => 
						  AkkaoogleActorServer.lookup("monitor") ! LogTimeout(proxy.path.name, "Timeout for " + fp)	
							Option.empty[LowestPrice]
          }
        }
        val lowestPrice: Future[Option[LowestPrice]] = findLowestPrice(futures)
        val totalPrice: Future[Option[LowestPrice]] = lowestPrice.map { l =>
          l.map(p => p.copy(price = p.price + (p.price * .02)))
        }
        totalPrice pipeTo sender
    }
}

class ExternalVendorProxyActor(val v: ExternalVendor) extends Actor {
    def receive = {
      case fp: FindPrice =>
        var result: Option[LowestPrice] = Option.empty[LowestPrice]
        val f = Future({
          val params = "?pd=" + fp.productDescription + "&q=" + fp.quantity
          val price = Source.fromURL(v.url + params).mkString.toDouble
          Some(LowestPrice(v.name, fp.productDescription, price * fp.quantity))
        }) recover { case t => Option.empty[LowestPrice] }
        f pipeTo sender
    }
}
