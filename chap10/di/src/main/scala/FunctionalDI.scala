package FunctionalStyleDI {

  trait Calculators {
	  type Calculator = String => Double
	  protected val findCalculator: String => Calculator
    protected val calculate: (Calculator, String) => Double = (calculator, productId) => calculator(productId)
  }

  object ProductionCalculators extends Calculators {
	  val costPlusCalculator: String => Double = productId => 0.0 
	  val externalPriceSource: String => Double = productId => 0.0 
	
	  override protected val findCalculator = Map(
      "costPlus" -> costPlusCalculator,
      "externalPriceSource" -> externalPriceSource
    )
	  def priceCalculator(priceType: String): String => Double = {
		   val f: Calculator => String => Double = calculate.curried
		   f(findCalculator(priceType))
		}
  }

  object TestCalculators extends Calculators {
	  val costPlusCalculator: String => Double = productId => 0.0 
	  val externalPriceSource: String => Double = productId => 0.0 
	
	  override protected val findCalculator = Map(
      "costPlus" -> costPlusCalculator,
      "externalPriceSource" -> externalPriceSource
    )

	  def priceCalculator(priceType: String): String => Double = {
		  val f: Calculator => String => Double = calculate.curried
		  f(findCalculator(priceType))
		}
  }

	
}