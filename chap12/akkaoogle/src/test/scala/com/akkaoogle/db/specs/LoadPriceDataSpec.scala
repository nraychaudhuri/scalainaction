package com.akkaoogle.db.specs

import org.specs2.mutable._
import com.akkaoogle.db.models._
import com.akkaoogle.db.AkkaoogleSchema
import com.akkaoogle.helpers._

class LoadPriceDataSpec extends Specification {

  step { 
	  H2Server.start()
	  AkkaoogleSchema.createSchema() 
	}

  "Product data loader" should {

    "add price for a product" in {
      val p = new Product(description = "product1",
        vendorName = "vendor joe",
        basePrice = 20.2,
        plusPercent = 10)
      p.save must beEqualTo(Right("Domain object is saved successfully"))
    }
  }

  step { 
	  H2Server.stop() 
	}
}