package example.actors {
  import scala.actors._
  import scala.actors.Actor._
  
  case class PlaceOrder(productId: String, quantity: Int, customerId: String)

  class OrderingService extends Actor {
    def act = {
      react {
        case PlaceOrder(productId, quantity, customer) =>
      }
    }
  }  
}
