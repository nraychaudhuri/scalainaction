trait OrderingSystem {
  type O <: Order
  type I <: Inventory
  type S <: Shipping
  
  trait Ordering {this: I with S =>
     def placeOrder(o: O): Option[Long] = {
	   println("placing order of class " + o.getClass.getName)  // the output you can see when running Main e.g. via sbt> run
	   
       if(itemExists(o)) {
         o.placeOrder(this)
         Some(scheduleShipping(o))
       }else None
     }
   }
  
  trait Order {def placeOrder(i: I):Unit }
  trait Inventory { def itemExists(order: O): Boolean }
  trait Shipping {def scheduleShipping(order: O): Long }
}

object BookOrderingSystem extends OrderingSystem {
  type O = BookOrder
  type I = AmazonBookStore
  type S = UPS

  class BookOrder extends Order { def placeOrder(i: AmazonBookStore): Unit = {} }  
  trait AmazonBookStore extends Inventory { def itemExists(o: BookOrder) = true }
  trait UPS extends Shipping { def scheduleShipping(order: BookOrder): Long = 1 }
  
  object BookOrdering extends Ordering with AmazonBookStore with UPS

}

object Main {
  def main(args: Array[String]) {
    import BookOrderingSystem._
    BookOrdering.placeOrder(new BookOrder)
  }
}