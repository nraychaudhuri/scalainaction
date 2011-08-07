package chap10.fp.examples

sealed trait Claim { val claimId: Int }
case class Full(val claimId: Int) extends Claim
case class Partial(val claimId: Int, percentage: Double) extends Claim
case class Generic(override val claimId: Int) extends Claim

case class Location(stateCode: Option[String], zipCode: Option[String])
case class Req(productId: String, location: Location, claim: Claim)
  
object PricingSystem {
  type PC = Tuple2[Req, Option[Double]]  
  
  def handleFullClaim: PartialFunction[PC, PC] = { 
    case (c@Req(id, l, Full(claimId)), basePrice)  =>  (c, basePrice.map(_ + 10))
  }
  
  def handlePartialClaim: PartialFunction[PC, PC] = { 
    case (c@Req(id, l, Partial(claimId, percentage)), basePrice)  =>  (c, basePrice.map(_ + 20))
  }
  
  def handleZipCode: PartialFunction[PC, PC] = { 
    case (c@Req(id, Location(_, Some(zipCode)), _), price) => (c, price.map(_ + 5)) 
  }
  
  def handleStateCode: PartialFunction[PC, PC] = { 
    case (c@Req(id, Location(Some(stateCode), _), _), price) => (c, price.map(_ + 10)) 
  }
  
  def claimHandlers = handleFullClaim orElse handlePartialClaim
  def locationHandlers = handleZipCode orElse handleStateCode
  
  def default: PartialFunction[PC, PC] = { case p => p }
  def priceCalculator: PartialFunction[PC, PC] = 
    (claimHandlers andThen locationHandlers) orElse default
  
  def main(args: Array[String]) = {
    priceCalculator((Req("some product", Location(None, Some("43230")), Full(1)), Some(10))) match {
      case (c, finalPrice) => println(finalPrice)
    }
    priceCalculator((Req("some product", Location(None, None), Generic(10)), Some(10))) match {
      case (c, finalPrice) => println(finalPrice)
    }
  }
  
}


object PFExample {
  def intToChar: PartialFunction[Int, Char] = {
    case 1 => 'a'
    case 3 => 'c'
  }  
  
  def throwableToInt: PartialFunction[Throwable, Int] = {
    case _ => 1
  }
  
  class IntToCharPF extends PartialFunction[Int, Char] {
    def apply(i: Int) = i match {
      case 1 => 'a'
      case 3 => 'c'
    }
    
    def isDefinedAt(i: Int): Boolean = i match {
      case 1 => true
      case 3 => true
      case _ => false
    }
  }
}
