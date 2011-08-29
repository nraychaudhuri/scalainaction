package chap10.monads.example

object StateMonad {
  import State._
  
  trait State[S, +A] {
    def apply(s: S): (S, A)
    def map[B](f: A => B): State[S, B] = state(apply(_) match {
      case (s, a) => (s, f(a))
    })

    def flatMap[B](f: A => State[S, B]): State[S, B] = state(apply(_) match {
      case (s, a) => f(a)(s)
    }) 
  }
  
  object State {
    def state[S, A](f: S => (S, A)) = new State[S, A] {
      def apply(s: S) = f(s)
    }     
    def init[S]: State[S, S] = state[S, S](s => (s, s))  
    def modify[S](f: S => S) = init[S] flatMap (s => state(_ => (f(s), ())))
    def gets[S,A](f: S => A): State[S, A] = init[S] flatMap (s => state(_ => (s, f(s)))) 
      // for (s <- init) yield f(s)
  }
}

object PriceCalculator {
  object Stubs {
    def findTheBasePrice(productId: String) = 10.0
    def findStateSpecificDiscount(productId: String, stateCode: String) = 0.5
    def findProductSpecificDiscount(productId: String) = 0.5
    def calculateTax(productId: String, price: Double) = 5.0
  }
  import Stubs._
  import StateMonad.State._
  case class PriceState(productId: String, stateCode: String,price: Double)
  
  def findBasePrice(ps: PriceState): Double = {
    val basePrice = findTheBasePrice(ps.productId)
    basePrice
  } 
  
  def applyStateSpecificDiscount(ps: PriceState): Double = { 
    val discount = findStateSpecificDiscount(ps.productId, ps.stateCode)
    ps.price - discount
  }
  
  def applyProductSpecificDiscount(ps: PriceState): Double = { 
    val discount = findProductSpecificDiscount(ps.productId)
    ps.price - discount
  }  
  def applyTax(ps: PriceState): Double = {
    val tax = calculateTax(ps.productId, ps.price)
    ps.price + tax
  }
  
  def calculatePrice2(productId: String, stateCode: String): Double = {  
    def modifyPriceState(f: PriceState => Double) = modify[PriceState](s => s.copy(price = f(s)))
    val stateMonad = modifyPriceState(findBasePrice) flatMap {a => 
      modifyPriceState(applyStateSpecificDiscount) flatMap {b => 
        modifyPriceState(applyProductSpecificDiscount) flatMap {c => 
          modifyPriceState(applyTax) map {d =>() }
        }
      } 
    }
    val initialPriceState = PriceState(productId, stateCode, 0.0)  
    val finalPriceState = stateMonad.apply(initialPriceState)._1
    val finalPrice = finalPriceState.price
    finalPrice
  }
    
  
  def calculatePrice(productId: String, stateCode: String): Double = {  
    def modifyPriceState(f: PriceState => Double) = modify[PriceState](s => s.copy(price = f(s)))
    val stateMonad = for { 
        _ <- modifyPriceState(findBasePrice)
        _ <- modifyPriceState(applyStateSpecificDiscount)
        _ <- modifyPriceState(applyProductSpecificDiscount)
        _ <- modifyPriceState(applyTax)
      } yield ()
    val initialPriceState = PriceState(productId, stateCode, 0.0)  
    val finalPriceState = stateMonad.apply(initialPriceState)._1
    val finalPrice = finalPriceState.price
    finalPrice
  }
    
  def calculatePriceWithLog(productId: String, stateCode: String): Double = {  
    def modifyPriceState(f: PriceState => Double) = modify[PriceState](s => s.copy(price = f(s)))
    def logStep(f: PriceState => String) = gets(f)
    val stateMonad = for { 
        _ <- modifyPriceState(findBasePrice)
        a <- logStep(s => "Base Price " + s)
        _ <- modifyPriceState(applyStateSpecificDiscount)
        b <- logStep(s => "After state discount " + s)
        _ <- modifyPriceState(applyProductSpecificDiscount)
        c <- logStep(s => "After product discount " + s)
        _ <- modifyPriceState(applyTax)
        d <- logStep(s => "After tax " + s)
      } yield a :: b :: c :: d :: Nil
    val (finalPriceState, log) = stateMonad.apply(PriceState(productId, stateCode, 0.0))
    println(">>>>>>>>> " + finalPriceState.price)
    println("log " + log)
    finalPriceState.price
  }
  
}