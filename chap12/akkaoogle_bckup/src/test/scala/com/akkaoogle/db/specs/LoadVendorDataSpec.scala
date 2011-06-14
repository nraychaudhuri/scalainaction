package com.akkaoogle.db.specs

import org.specs._
import com.akkaoogle.db.models._
import java.math.{BigDecimal => JBD }
import org.h2.tools.Server
import com.akkaoogle.db.AkkaoogleSchema

class LoadVendorDataSpec extends Specification {
  val server = Server.createTcpServer().start()

  "ExternalVendor data loader" should {

    doFirst { AkkaoogleSchema.createSchema() }

    "add vendor with url" in {
      val v = new ExternalVendor(name = "amazon", url = "http://www.amz.com")
      v.save must beEqual(Right("Domain object is saved successfully"))
    }

    "find all the vendors in the database" in {
      AkkaoogleSchema.createSchema()
      new ExternalVendor(name = "amazon", url = "http://www.amz.com").save
      new ExternalVendor(name = "ebay", url = "http://www.ebay.com").save
      new ExternalVendor(name = "auction", url = "http://www.auction.com").save
      ExternalVendor.findAll.size must beEqualTo(3)
    }

    doLast { server.stop() }
  }
}