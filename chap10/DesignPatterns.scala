def calculatePrice(product: String, taxingStrategy: String => Double) = {
  val tax = taxingStrategy(product)
}