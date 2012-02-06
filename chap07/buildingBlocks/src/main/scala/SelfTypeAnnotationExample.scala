package chap07.example.selfTypeAnnotation {
  trait Connection {
    def query(q: String): String
  }
  trait Logger {
    def log(l: String): Unit
  }
  
  trait RequiredServices {
    def makeDatabaseConnection: Connection
    def logger: Logger
  }

  trait TestServices extends RequiredServices {
    def makeDatabaseConnection = new Connection { def query(q: String) = "test" }
    def logger = new Logger { def log(l: String) = println(l) }
  }
  
  trait ProductFinder { this: RequiredServices =>
    def findProduct(productId: String) = {
      val c = makeDatabaseConnection
      c.query(productId)
      logger.log("querying database..")
    }
  }
  object FinderSystem extends ProductFinder with TestServices  
}


