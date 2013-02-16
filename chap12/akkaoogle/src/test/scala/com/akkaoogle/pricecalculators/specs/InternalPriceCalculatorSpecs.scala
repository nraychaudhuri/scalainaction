package com.akkaoogle.pricecalculators.specs

import org.specs2.mutable._

import com.akkaoogle.calculators._
import com.akkaoogle.db.models._
import org.h2.tools.Server
import akka.actor._
import com.akkaoogle.calculators.messages._
import com.akkaoogle.infrastructure._
import com.akkaoogle.db.AkkaoogleSchema
import akka.pattern.{ ask, pipe }
import scala.concurrent.{Await, Future}
import akka.util.Timeout
import org.specs2.specification.Scope
import com.akkaoogle.helpers._
import scala.concurrent.ExecutionContext.Implicits.global

class InternalPriceCalculatorSpecs extends Specification {
  sequential

  trait WithSchema extends Scope with BeforeAfter {
	
	  def before = {
	    H2Server.start()
	    AkkaoogleSchema.createSchema()
	    AkkaoogleActorServer.run()		
  	}

    def after = {
      H2Server.stop()
      AkkaoogleActorServer.stop()
    }
  }

  implicit val timeout = new Timeout(1000, java.util.concurrent.TimeUnit.MILLISECONDS)

  "Product calculation for products we sell" should {
    "apply plus percent to base price to calculate price" in new WithSchema {
      val productDescription = "XYZ"
      new Product(productDescription, "vendorA", 100, 10.0).save
      val a = AkkaoogleActorServer.lookup("internal-price-calculator0")
      val future = a ? FindPrice(productDescription, 1)
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "calculate for multiple quantity" in new WithSchema {
      val productDescription = "XYZ"
      new Product(productDescription, "vendorA", 100, 10.0).save
      val a = AkkaoogleActorServer.lookup("internal-price-calculator0")
      val future = a ? FindPrice(productDescription, 2)
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 220.0)))
    }

    "calculate for 10 concurrent buyers" in new WithSchema {
      (1 to 10) foreach { id => new Product("p" + id, "v" + id, 100 * id, 10.0 * id).save }
      val calculatorActor = AkkaoogleActorServer.lookup("internal-price-calculator0")
      val results = (1 to 10) map { i => (calculatorActor ? FindPrice("p" + i, 1)).mapTo[Option[LowestPrice]] }
      val future = Future.sequence(results)
      val result = Await.result(future, timeout.duration)
      result.size must beEqualTo(10)
      result foreach { resultOption => resultOption.isDefined must beTrue }
    }

    "calculate price when distributed to remote nodes" in new WithSchema {
      val remoteCalculator = AkkaoogleActorServer.lookup("internal-load-balancer")
      val productId = "XYZ"
      new Product(productId, "vendor cool", 100, 10.0).save
      val future = remoteCalculator ? FindPrice("XYZ", 1)
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendor cool", "XYZ", 110.0)))
    }
  }
}