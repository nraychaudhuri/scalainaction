package com.akkaoogle.pricecalculators.specs

import org.specs2.mutable._
import com.akkaoogle.infrastructure.RemoteActorServer
import org.h2.tools.{ Server => H2Server }
import com.akkaoogle.db.models._
import com.akkaoogle.helpers.FakeExternalVendor
import com.akkaoogle.calculators.messages.{LowestPrice, FindPrice}
import com.akkaoogle.db.AkkaoogleSchema

class ExternalPriceCalculatorSpecs
  extends Specification
  with FakeExternalVendor
  {
   val h2Server = H2Server.createTcpServer().start()

   "Product calculation for products sold by external vendors" should {

     doBefore {
       AkkaoogleSchema.createSchema()
     }

     "apply 2% service charge on the base price" in {
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse {
         r =>
           r match {
              case "/test" => 20.0D
              case _ => 1000.0D
           }
       }
       RemoteActorServer.run()
       val remoteCalculator = RemoteActorServer.lookup("external-load-balancer")
       val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("test", "XYZ", 20.4)))
     }

     "timeout when price is not received within 200 milliseconds" in {
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>
         println("Waiting for 250 millis")
         Thread.sleep(250L)
         30.0D
       }
       RemoteActorServer.run()
       val remoteCalculator = RemoteActorServer.lookup("external-load-balancer")
       val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(None)
     }

     "return the least price from multiple vendors" in {
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
       RemoteActorServer.run()
       val remoteCalculator = RemoteActorServer.lookup("external-load-balancer")
       val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 20.4D)))
     }

     "ignore responses received after timeout" in {
       new ExternalVendor(name = "vendorA", url = "http://localhost:8080/vendorA").save
       new ExternalVendor(name = "vendorB", url = "http://localhost:8080/vendorB").save
       new ExternalVendor(name = "vendorC", url = "http://localhost:8080/vendorC").save
       new ExternalVendor(name = "vendorD", url = "http://localhost:8080/vendorD").save
       addFakeExternalResponse {
         r =>
           r match {
              case "/vendorA" => Thread.sleep(250); 20.0D
              case "/vendorB" => 40.0D
              case "/vendorC" => Thread.sleep(210); 10.0D
              case "/vendorD" => 30.0D
              case _ => 1000.0D
           }
       }
       RemoteActorServer.run()
       val remoteCalculator = RemoteActorServer.lookup("external-load-balancer")
       val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("vendorD", "XYZ", 30.6D)))
     }

     "log message when a external vendor response timesout" in {
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
       RemoteActorServer.run()
       val remoteCalculator = RemoteActorServer.lookup("external-load-balancer")
       val resultOption = remoteCalculator !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("vendorB", "XYZ", 40.8D)))
       val logs = TransactionFailure.findAll
       logs.size must beEqualTo(1)
       logs.head.vendorId must beEqualTo("vendorA")
     }


     doAfter {
       server.stop()
       RemoteActorServer.stop()
     }

     doLast {
       h2Server.stop()
     }
   }
}