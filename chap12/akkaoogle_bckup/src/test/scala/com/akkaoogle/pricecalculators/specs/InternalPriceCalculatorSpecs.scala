package com.akkaoogle.pricecalculators.specs

import org.specs._

import com.akkaoogle.calculators.pricecalculators._
import com.akkaoogle.db.models._
import org.h2.tools.Server
import akka.actor._
import com.akkaoogle.calculators.messages._
import com.akkaoogle.infrastructure._
import com.akkaoogle.db.AkkaoogleSchema

class InternalPriceCalculatorSpecs extends Specification {

  val server = Server.createTcpServer().start()

  "Product calculation for products we sell" should {

    doFirst {
      AkkaoogleSchema.createSchema()
      RemoteActorServer.run()
    }

    "apply plus percent to base price to calculate price" in {
      val productDescription = "XYZ"
      new Product(productDescription, "vendorA", 100, 10.0).save
      val a = Actor.actorOf(new InternalPriceCalculator).start
      val resultOption = a !! FindPrice(productDescription, 1)
      resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "calculate for multiple quantity" in {
      val productDescription = "XYZ"
      new Product(productDescription, "vendorA", 100, 10.0).save
      val a = Actor.actorOf(new InternalPriceCalculator).start
      val resultOption = a !! FindPrice(productDescription, 2)
      resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 220.0)))
    }

    "calculate for 10 concurrent buyers" in {
      (1 to 10) foreach { id => new Product("p" + id, "v" + id, 100 * id, 10.0 * id).save }
      val calculatorActor = Actor.actorOf(new InternalPriceCalculator).start
      val results = (1 to 10) map { i => calculatorActor !! FindPrice("p" + i, 1)  }
      results.size must beEqualTo(10)
      results foreach { resultOption =>
        resultOption.isDefined must be(true)
      }
    }

    "calculate price when distributed to remote nodes" in {
      val remoteCalculator = RemoteActorServer.lookup("internal-load-balancer")
      val productId = "XYZ"
      new Product(productId, "vendor cool", 100, 10.0).save
      val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
      resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    doLast {
      server.stop()
      RemoteActorServer.stop()
    }
  }
}