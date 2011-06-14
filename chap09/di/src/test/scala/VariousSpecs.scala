package variousspecs
import org.specs._
import cakepattern._

object MySpec extends Specification {
  "example1" in {}
  "example2" in {}
}

object SUTSpec extends Specification {
  "my system" should {
    "do this" in {}
    "do that" in {}    
  }
  
  "my system" should { 
    doBefore {initSystem  }
    doAfter { resetSystem  }
    "mess up the system" in {}
    "and again" in {}
    
  }
  
  def resetSystem = println("tear down")
  def initSystem = println("set up")
}


import org.specs.util.DataTables
object PricingSystemSpec extends Specification with TestPricingSystem {
  "Calculate price service" should {
    "calculate price for cost plus price type" in {
      val service = new CalculatePriceService
      val price: Double = service.calculate("costPlus", "some product")
      price must beEqualTo(5.0D)        
      
      "for empty product id return 0.0" in {
        val service = new CalculatePriceService
        service.calculate("costPlus", "") must beEqualTo(0.0D)
      }      
    }
    
    "calculate price for external price source type" in {
      val service = new CalculatePriceService
      val price: Double = service.calculate("externalPriceSource", "some product")
      price must be_==(10.0D)      
    }
  }
}


object CostPlusRulesSpec extends Specification with DataTables {
  
  def applyCostPlusBusinessRule(cost: Double, serviceCharge: Double) = {
    cost + (cost * 0.2) + serviceCharge
  }
  
  "cost plus price is calculated using 'cost + 20% of cost + given service charge' rule" in {
    "cost" | "service charge" | "price" |>
    100.0  !   4              ! 124     |  
    200.0  !   4              ! 244     |  
    0.0  !     2              ! 2       | { (cost, serviceCharge, expected) =>
      applyCostPlusBusinessRule(cost, serviceCharge) must be_==(expected)
    }    
  }
}