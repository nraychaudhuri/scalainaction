
//Implementing a template method pattern by abstracting the type
package abstractMember {
  trait Calculator {
    type S
    def initialize: S
    def close(s: S): Unit
    def calculate(productId: String): Double = {
      val s = initialize
      val price = calculate(s, productId)
      close(s)
      price
    }
    def calculate(s: S, productId: String): Double
  }  
  
  class CostPlusCalculator extends Calculator {
    type S = CostPlusDao
    def initialize = new CostPlusDao
    def close(dao: CostPlusDao) = dao.close
    
    def calculate(source: CostPlusDao, productId: String) = {
      0.0
    }
  }
  
  class CostPlusDao {
    def close = {}
  }
}
