package com.akkaoogle.pricecalculators.specs

import org.specs2.mutable._
import org.specs2.specification.Scope
import com.akkaoogle.infrastructure.AkkaoogleActorServer
import com.akkaoogle.db.models._
import com.akkaoogle.helpers.{FakeExternalVendor, H2Server}
import com.akkaoogle.calculators.messages.{ LowestPrice, FindPrice }
import com.akkaoogle.db.AkkaoogleSchema
import akka.pattern.{ ask, pipe }
import scala.concurrent.Await
import akka.util.Timeout


class ExternalPriceCalculatorSpecs
  extends Specification
  with FakeExternalVendor {

  sequential

  step { H2Server.start() }
  
  trait WithSchema extends Scope with BeforeAfter {
    
    def before = {
	    AkkaoogleSchema.createSchema()
	  }

    override def after = {
      AkkaoogleActorServer.stop()
      server.stop()
    }
  }

  implicit val timeout = new Timeout(500, java.util.concurrent.TimeUnit.MILLISECONDS)

  "Product calculation for products sold by external vendors" should {

    "apply 2% service charge on the base price" in new WithSchema {
      new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
      addFakeExternalResponse {
        r =>
          r match {
            case "/test" => 20.0D
            case _ => 1000.0D
          }
      }
      AkkaoogleActorServer.run()
      val remoteCalculator = AkkaoogleActorServer.lookup("external-load-balancer")
      val future = (remoteCalculator ? FindPrice("XYZ", 1)).mapTo[Option[LowestPrice]]
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("test", "XYZ", 20.4)))
    }

    "timeout when price is not received within 200 milliseconds" in new WithSchema {
      new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
      addFakeExternalResponse { r =>
        println("Waiting for 250 millis")
        Thread.sleep(250L)
        30.0D
      }
      AkkaoogleActorServer.run()
      val remoteCalculator = AkkaoogleActorServer.lookup("external-load-balancer")
      val future = remoteCalculator ? FindPrice("XYZ", 1)
      Await.result(future, timeout.duration) must equalTo(None)
    }

    "return the least price from multiple vendors" in new WithSchema {
      new ExternalVendor(name = "vendorA", url = "http://localhost:8080/vendorA").save
      new ExternalVendor(name = "vendorB", url = "http://localhost:8080/vendorB").save
      new ExternalVendor(name = "vendorC", url = "http://localhost:8080/vendorC").save
      addFakeExternalResponse {
        r =>
          r match {
            case "/vendorA" => 20.0D
            case "/vendorB" => 30.0D
            case "/vendorC" => 40.0D
            case _ => 1000.0D
          }
      }
      AkkaoogleActorServer.run()
      val remoteCalculator = AkkaoogleActorServer.lookup("external-load-balancer")
      val future = (remoteCalculator ? FindPrice("XYZ", 1)).mapTo[Option[LowestPrice]]
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 20.4D)))
    }

  
    "ignore responses received after timeout" in new WithSchema {
      new ExternalVendor(name = "vendorA", url = "http://localhost:8080/vendorA").save
      new ExternalVendor(name = "vendorB", url = "http://localhost:8080/vendorB").save
      new ExternalVendor(name = "vendorC", url = "http://localhost:8080/vendorC").save
      new ExternalVendor(name = "vendorD", url = "http://localhost:8080/vendorD").save
      addFakeExternalResponse {
        r =>
          r match {
            case "/vendorA" => Thread.sleep(250); 20.0D
            case "/vendorB" => 40.0D
            case "/vendorC" => Thread.sleep(250); 10.0D
            case "/vendorD" => 30.0D
            case _ => 1000.0D
          }
      }
      AkkaoogleActorServer.run()
      val remoteCalculator = AkkaoogleActorServer.lookup("external-load-balancer")
      val future = (remoteCalculator ? FindPrice("XYZ", 1)).mapTo[Option[LowestPrice]]
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendorD", "XYZ", 30.6D)))
    }

    "log message when a external vendor response timesout" in new WithSchema {
      new ExternalVendor(name = "vendorA", url = "http://localhost:8080/vendorA").save
      new ExternalVendor(name = "vendorB", url = "http://localhost:8080/vendorB").save
      addFakeExternalResponse {
        r =>
          r match {
            case "/vendorA" => Thread.sleep(250); 20.0D
            case "/vendorB" => 40.0D
            case _ => 1000.0D
          }
      }
      AkkaoogleActorServer.run()
      TransactionFailure.findAll.size must beEqualTo(0)
      
			val remoteCalculator = AkkaoogleActorServer.lookup("external-load-balancer")
      val future = remoteCalculator ? FindPrice("XYZ", 1)
      val result = Await.result(future, timeout.duration)
      result must beEqualTo(Some(LowestPrice("vendorB", "XYZ", 40.8D)))
      val logs = TransactionFailure.findAll
      logs.foreach(f => println("id " + f.id + " , message = " + f.message))
      logs.size must beEqualTo(1)
      logs.head.vendorId must beEqualTo("vendorA")
    }
  }

  step { H2Server.stop() }

}


