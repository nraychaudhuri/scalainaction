package cakepattern {
trait CalculatePriceServiceComponent {this: Calculators =>
  class CalculatePriceService {  
    val calculators = Map(
      "costPlus" -> calculate(costPlusCalculator) _ ,
      "externalPriceSource" -> calculate(externalPriceSourceCalculator) _)

    def calculate(priceType: String, productId: String): Double = {
      if(productId.isEmpty) 0.0D
      else calculators(priceType)(productId)
    }
    private[this] def calculate(c: Calculator)(productId: String):Double = c.calculate(productId)
  }  
}

trait Calculators {
  val costPlusCalculator: CostPlusCalculator
  val externalPriceSourceCalculator: ExternalPriceSourceCalculator
  
  trait Calculator {
    def calculate(productId: String): Double
  }

  class CostPlusCalculator extends Calculator {
    def calculate(productId: String) = {
      0.0
    }
  }

  class ExternalPriceSourceCalculator extends Calculator {
    def calculate(productId: String) = {
      0.0
    }
  } 
}

object PricingSystem extends CalculatePriceServiceComponent with Calculators {
  val costPlusCalculator = new CostPlusCalculator
  val externalPriceSourceCalculator = new ExternalPriceSourceCalculator
}

trait TestPricingSystem extends CalculatePriceServiceComponent with Calculators {
  class StubCostPlusCalculator extends CostPlusCalculator {
    override def calculate(productId: String) = 5.0D
  }
  class StubExternalPriceSourceCalculator extends ExternalPriceSourceCalculator {
    override def calculate(productId: String) = 10.0D
  }
  val costPlusCalculator = new StubCostPlusCalculator
  val externalPriceSourceCalculator = new StubExternalPriceSourceCalculator
}
}
