// You can run this in the REPL in chap10/ with 
// scala :load DesignPatterns.scala
// or the paste mode see scala> :help

object DesignPatterns {
  //Stratgey pattern
  def calculatePrice(product: String, taxingStrategy: String => Double) = {
    val tax = taxingStrategy(product)
  }

  //DI in functional style
  trait TaxStrategy {def taxIt(product: String): Double }
  class ATaxStrategy extends TaxStrategy {
    def taxIt(product: String): Double = 10.0
  }
  class BTaxStrategy extends TaxStrategy {
    def taxIt(product: String): Double = 20.0
  }

  def taxIt: TaxStrategy => String => Double = s => p => s.taxIt(p)

  def taxIt_a: String => Double = taxIt(new ATaxStrategy)  
  def taxIt_b: String => Double = taxIt(new BTaxStrategy)  
}
