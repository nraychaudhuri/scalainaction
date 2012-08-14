package com.akkaoogle.calculators
import messages._
import com.akkaoogle.db.models._
import akka.actor._
import scala.io.Source
import akka.dispatch.Future
import akka.pattern.{ ask, pipe, AskTimeoutException }
import akka.dispatch.ExecutionContext
import akka.pattern.AskTimeoutException
import com.akkaoogle.infrastructure.RemoteActorServer


class ExternalPriceCalculator(val proxies: Iterable[ActorRef]) extends Actor {
    def receive = {
      case FindPrice(productId, quantity) =>
        val futures = proxies map { proxy =>
	        val fp = FindPrice(productId, quantity)
          (proxy ? fp).mapTo[Option[LowestPrice]] recover {
            case e: AskTimeoutException => Option.empty[LowestPrice]
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
        //TODO find a better way to do this
        val f = Future({
          val params = "?pd=" + fp.productDescription + "&q=" + fp.quantity
          val price = Source.fromURL(v.url + params).mkString.toDouble
          Some(LowestPrice(v.name, fp.productDescription, price * fp.quantity))
        }) recover { case t => Option.empty[LowestPrice] }

        val timeoutFuture = Future {
          Thread.sleep(150)
          Option.empty[LowestPrice]
        }
        val firstCompletedFuture = Future.firstCompletedOf(List(f, timeoutFuture))
        firstCompletedFuture foreach {
	          case None => RemoteActorServer.lookup("monitor") ! LogTimeout(v.name, "Timeout for " + fp)	
	          case _ =>
        } 
        firstCompletedFuture pipeTo sender
    }
}
