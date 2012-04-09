
//Implementing a template method pattern by abstracting the type
package chap07.example.abstractMember {
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
    type S = MongoClient
    def initialize = new MongoClient
    def close(dao: MongoClient) = dao.close
    
    def calculate(source: MongoClient, productId: String) = {
      0.0
    }
  }
  
  class MongoClient {
    def close = {}
  }
}
