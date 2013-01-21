package structuralTyping {
  import scala.language.reflectiveCalls
  
  class CalculatePriceService(c: {
    val costPlusCalculator: Calculator
    val externalPriceSourceCalculator: Calculator      
  }) {  
    val calculators = Map(
      "costPlus" -> calculate(c.costPlusCalculator) _ ,
      "externalPriceSource" -> calculate(c.externalPriceSourceCalculator) _)

    def calculate(priceType: String, productId: String): Double = {
      calculators(priceType)(productId)
    }
    private[this] def calculate(c: Calculator)(productId: String):Double = c.calculate(productId)
  }  
  
  
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
  object ProductionConfig {
    val costPlusCalculator = new CostPlusCalculator
    val externalPriceSourceCalculator = new ExternalPriceSourceCalculator
    val priceService = new CalculatePriceService(this)
  }
  
  object TestConfig {
    val costPlusCalculator = new CostPlusCalculator {
      override def calculate(productId: String) = 0.0
    }
    
    val externalPriceSourceCalculator = new ExternalPriceSourceCalculator {
      override def calculate(productId: String) = 0.0      
    }
    val priceService = new CalculatePriceService(this)
  }
  
  
}

