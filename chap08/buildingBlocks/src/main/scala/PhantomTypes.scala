package phantomtypes

sealed trait OrderCompleted
sealed trait InCompleteOrder
sealed trait ItemProvided
sealed trait NoItem
sealed trait AddressProvided
sealed trait NoAddress

case class Order[A, B, C](itemId: Option[String], shippingAddress: Option[String])

object Order {
  def emptyOrder = Order[InCompleteOrder, NoItem, NoAddress](None, None)
}

object OrderingSystem {
  def addItem[A, B](item: String, o: Order[A, NoItem, B]) = o.copy[A, ItemProvided, B](itemId = Some(item))
  def addShipping[A, B](address: String, o: Order[A, B, NoAddress]) = o.copy[A, B, AddressProvided](shippingAddress = Some(address))
  

  def placeOrder(o: Order[InCompleteOrder, ItemProvided, AddressProvided]) = {
    o.copy[OrderCompleted, ItemProvided, AddressProvided]()
  }
  
  def main(args: Array[String]): Unit = {
    val o = Order.emptyOrder
    val o1 = addItem("some book", o)
    val o2 = addShipping("some address", o1)
    placeOrder(o2)
  }
}

