package actorspec

import org.specs2.mutable._
import example.actors._ 


import com.jayway.awaitility.scala._
import com.jayway.awaitility.Awaitility._

class OrderingServiceSpecification extends Specification with AwaitilitySupport {
  "Ordering system" should {
    "place order asynchronously" in {
      val s = new OrderingService().start
      s ! PlaceOrder("product id", 1, "some customer id")      
      await until { orderSavedInDatabase("some customer id") }
	  
      done
    }
  }
  def orderSavedInDatabase(customerId: String) = true
  
}


// 
// class CalculatePriceServiceActorsSpecification extends Specification {
//   import CalculatePriceServiceActors._
//   "actor based calculate price sevice" should {
//     "calculate price asynchromously" in {
//       var price = 0.0D
//       val m = ("costPlus", "some product") 
//       calculatePriceService !! m
//     }
//   }
// }
// 
// object CalculatePriceServiceActors {
//   val calculatePriceService = actor {    
//     react {
//       case (priceType, productId) => priceType match {
//         case "costPlus" => costPlusCalculator !? productId
//       }      
//     }
//   }
//   
//   val costPlusCalculator = actor {
//     react {
//       case productId => 10.0
//     }
//   }
// }