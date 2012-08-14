package com.akkaoogle.db.specs

import org.specs2.mutable._
import com.akkaoogle.db.models._
import java.math.{BigDecimal => JBD }
import org.h2.tools.Server
import com.akkaoogle.db.AkkaoogleSchema
import org.specs2.specification.Scope
import com.akkaoogle.helpers._

class LoadVendorDataSpec extends Specification {
	sequential

  step {  H2Server.start() }

  trait WithSchema extends Scope { AkkaoogleSchema.createSchema() }

  "ExternalVendor data loader" should {
    "add vendor with url" in new WithSchema {
      val v = new ExternalVendor(name = "amazon", url = "http://www.amz.com")
      v.save must beEqualTo(Right("Domain object is saved successfully"))
    }
	  "find all the vendors in the database" in new WithSchema {
			new ExternalVendor(name = "amazon", url = "http://www.amz.com").save
			new ExternalVendor(name = "ebay", url = "http://www.ebay.com").save
			new ExternalVendor(name = "auction", url = "http://www.auction.com").save
			ExternalVendor.findAll.size must beEqualTo(3)
		}
  }

  step { H2Server.stop() }
}