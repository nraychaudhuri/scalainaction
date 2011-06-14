package implicitParameter {
  
  class CalculatePriceService(
    implicit val costPlusCalculator: CostPlusCalculator,
    implicit val externalPriceSourceCalculator: ExternalPriceSourceCalculator      
  ) {  
    val calculators = Map(
      "costPlus" -> calculate(costPlusCalculator) _ ,
      "externalPriceSource" -> calculate(externalPriceSourceCalculator) _)

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
  
  object ProductionServices {
    implicit val costPlusCalculator = new CostPlusCalculator
    implicit val externalPriceSourceCalculator = new ExternalPriceSourceCalculator
  }
   
  object ProductionConfig {
    import ProductionServices._
    val priceService = new CalculatePriceService
  }
  
  object TestServices {
    implicit val costPlusCalculator = new CostPlusCalculator {
      override def calculate(productId: String) = 0.0
    }
      
    implicit val externalPriceSourceCalculator = new ExternalPriceSourceCalculator {
      override def calculate(productId: String) = 0.0      
    }
  }
    
  object TestConfig {
    import TestServices._
    val priceService = new CalculatePriceService
  }
}

