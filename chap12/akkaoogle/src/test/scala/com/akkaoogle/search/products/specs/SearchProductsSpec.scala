package com.akkaoogle.search.products.specs

import org.specs2.mutable._
import org.h2.tools.{ Server => H2Server }
import com.akkaoogle.infrastructure.RemoteActorServer
import com.akkaoogle.helpers.FakeExternalVendor
import com.akkaoogle.db.models._
import com.akkaoogle.calculators.messages.{LowestPrice, FindPrice}
import com.akkaoogle.db.AkkaoogleSchema

class SearchProductsSpec
  extends Specification
  with FakeExternalVendor
  {

  val h2Server = H2Server.createTcpServer().start()

  "Akkaoogle search finds the cheapest deal" should {

     doBefore {
       AkkaoogleSchema.createSchema()
     }

    "find cheapest deal from internal when external vendors are more" in {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  200.0D }
       RemoteActorServer.run()

       val finder = RemoteActorServer.lookup("cheapest-deal-finder")
       val resultOption = finder !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "find cheapest deal from external when internal vendors are more" in {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  50.0D }
       RemoteActorServer.run()

       val finder = RemoteActorServer.lookup("cheapest-deal-finder")
       val resultOption = finder !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("test", "XYZ", 51.0)))
    }

    "find deal from internal vendor when external times out" in {
       new Product("XYZ", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r =>  Thread.sleep(200L); 50.0D }
       RemoteActorServer.run()

       val finder = RemoteActorServer.lookup("cheapest-deal-finder")
       val resultOption = finder !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("vendorA", "XYZ", 110.0)))
    }

    "find deal from external when internally not provided" in {
       new Product("ABC", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       addFakeExternalResponse { r => 50.0D }
       RemoteActorServer.run()

       val finder = RemoteActorServer.lookup("cheapest-deal-finder")
       val resultOption = finder !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(Some(LowestPrice("test", "XYZ", 51.0)))
    }

    "find nothing when product is not supported" in {
       new Product("ABC", "vendorA", 100, 10.0).save
       new ExternalVendor(name = "test", url = "http://localhost:8080/test").save
       RemoteActorServer.run()

       val finder = RemoteActorServer.lookup("cheapest-deal-finder")
       val resultOption = finder !! FindPrice("XYZ", 1)
       resultOption.get must beEqualTo(None)
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