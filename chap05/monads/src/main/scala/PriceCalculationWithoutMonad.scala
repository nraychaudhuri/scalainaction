package chap10.monads.example

object PriceCalculatorWithoutMonad {
  object Stubs {
    def findTheBasePrice(productId: String) = 10.0
    def findStateSpecificDiscount(productId: String, stateCode: String) = 0.5
    def findProductSpecificDiscount(productId: String) = 0.5
    def calculateTax(productId: String, price: Double) = 5.0
  }
  import Stubs._
  case class PriceState(productId: String, stateCode: String,price: Double)
  
  def findBasePrice(productId: String, stateCode: String): PriceState = {
    val basePrice = findTheBasePrice(productId: String)
    PriceState(productId, stateCode, basePrice)
  } 
  
  def applyStateSpecificDiscount(ps: PriceState): PriceState = { 
    val discount = findStateSpecificDiscount(ps.productId, ps.stateCode)
    ps.copy(price = ps.price - discount)
  }
  
  def applyProductSpecificDiscount(ps: PriceState): PriceState = { 
    val discount = findProductSpecificDiscount(ps.productId)
    ps.copy(price = ps.price - discount)
  }  
  def applyTax(ps: PriceState): PriceState = {
    val tax = calculateTax(ps.productId, ps.price)
    ps.copy(price = ps.price + tax)    
  }  
  
  def calculatePrice(productId: String, stateCode: String): Double = {
    val a = findBasePrice(productId, stateCode)
    val b = applyStateSpecificDiscount(a)
    val c = applyProductSpecificDiscount(b)
    val d = applyTax(c)
    d.price
  }
}

