package com.akkaoogle.db.specs

import org.specs._
import com.akkaoogle.db.models._
import org.h2.tools.Server
import com.akkaoogle.db.AkkaoogleSchema

class LoadPriceDataSpec extends Specification {
  val server = Server.createTcpServer().start()
  "Product data loader" should {

    doFirst { AkkaoogleSchema.createSchema() }

    "add price for a product" in {
      val p = new Product(description = "product1",
        vendorName = "vendor joe",
        basePrice = 20.2,
        plusPercent = 10)
      p.save must beEqual(Right("Domain object is saved successfully"))
    }

    doLast { server.stop() }
  }
}