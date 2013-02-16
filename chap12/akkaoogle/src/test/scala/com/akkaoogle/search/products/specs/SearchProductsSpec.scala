package com.akkaoogle.search.products.specs

import org.specs2.mutable._
import org.specs2.specification.Scope
import com.akkaoogle.infrastructure.AkkaoogleActorServer
import com.akkaoogle.helpers.FakeExternalVendor
import com.akkaoogle.db.models._
import com.akkaoogle.calculators.messages.{LowestPrice, FindPrice}
import com.akkaoogle.db.AkkaoogleSchema
import akka.pattern.{ask, pipe }
import scala.concurrent.Await
import akka.util.Timeout
import com.akkaoogle.helpers._

class SearchProductsSpec
  extends Specification
  with FakeExternalVendor
  {
	
	sequential

  step { H2Server.start() }

	implicit val timeout = new Timeout(200, java.util.concurrent.TimeUnit.MILLISECONDS)

  trait WithSchema extends Scope with BeforeAfter {
	 
	  def before = { AkkaoogleSchema.createSchema() }
	
    def after = {
	    server.stop()
      AkkaoogleActorServer.stop()
    }
  }

  "Akkaoogle search finds the cheapest deal" should {

    "find cheapest deal from internal when external vendors are more" in new WithSchema {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  200.0D }
       AkkaoogleActorServer.run()

       val finder = AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer")
       val future = finder ? FindPrice("XYZ", 1)
			 val result = Await.result(future, timeout.duration)
       result must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "find cheapest deal from external when internal vendors are more" in new WithSchema {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  50.0D }
       AkkaoogleActorServer.run()

       val finder = AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer")
       val future = finder ? FindPrice("XYZ", 1)
			 val result = Await.result(future, timeout.duration)
       result must beEqualTo(Some(LowestPrice("test", "XYZ", 51.0)))
    }

    "find deal from internal vendor when external times out" in new WithSchema {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  Thread.sleep(200L); 50.0D }
       AkkaoogleActorServer.run()

       val finder = AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer")
       val future = finder ? FindPrice("XYZ", 1)
			 val result = Await.result(future, timeout.duration)
       result must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "find deal from external when internally not provided" in new WithSchema {
       new Product("ABC", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r => 50.0D }
       AkkaoogleActorServer.run()

       val finder = AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer")
       val future = finder ? FindPrice("XYZ", 1)
			 val result = Await.result(future, timeout.duration)
       result must beEqualTo(Some(LowestPrice("test", "XYZ", 51.0)))
    }

    "find nothing when product is not supported" in new WithSchema {
       new Product("ABC", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       AkkaoogleActorServer.run()

       val finder = AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer")
       val future = finder ? FindPrice("XYZ", 1)
			 val result = Await.result(future, timeout.duration)
       result must beEqualTo(None)
    }
  }

  step {
    H2Server.stop()
  }

}