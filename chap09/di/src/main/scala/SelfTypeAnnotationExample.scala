package selfTypeAnnotation {
  trait Connection {
    def query(q: String): Double = { 0.0 }
  }
  trait Logger {
    def log(l: String) = {println(l) }
  }
  
  trait RequiredServices {
    def makeDatabaseConnection: Connection
    def logger: Logger
  }

  trait TestServices extends RequiredServices {
    def makeDatabaseConnection = new Connection {}
    def logger = new Logger {}
  }
  
  class PriceFinder { this: RequiredServices =>
    def findPrice(productId: String) = {
      val c = makeDatabaseConnection
      c.query("find the lowest price")
    }
  }
  object PricingSystem extends PriceFinder with TestServices  
}


