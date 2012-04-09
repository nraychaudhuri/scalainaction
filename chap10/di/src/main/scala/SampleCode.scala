
sealed class CalculatePriceService {  
  val costPlusCalculator = new CostPlusCalculator()
  val externalPriceSourceCalculator = new ExternalPriceSourceCalculator()
  
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
