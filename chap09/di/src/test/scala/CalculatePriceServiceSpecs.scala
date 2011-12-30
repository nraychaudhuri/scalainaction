import org.specs2.mutable._
import cakepattern._
class CalculatePriceServiceSpecification extends Specification with TestPricingSystem {
  "Calculate price service" should {
    "calculate price for cost plus price type" in {
      val service = new CalculatePriceService
      val price: Double = service.calculate("costPlus", "some product")
      price must beEqualTo(5.0D)
    }
    "calculate price for external price source type" in {
      val service = new CalculatePriceService
      val price: Double = service.calculate("externalPriceSource", "some product")
      price must be_==(10.0D)      
    }
  }
}