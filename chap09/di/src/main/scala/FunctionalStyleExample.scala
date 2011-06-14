/*

trait TradeRepository {
  def fetch(refNo: String): Trade
  def update(trade: Trade): Trade
  def write(trade: Trade): Unit
}

// service functions
trait TradeService {
  val fetchTrade: TradeRepository => String => Trade = {repo => refNo => repo.fetch(refNo)}
  val updateTrade: TradeRepository => Trade => Trade = {repo => trade => //..
  val writeTrade: TradeRepository => Trade => Unit = {repo => trade => repo.write(trade)}
}


Now let's say we would like to work with a Redis based implementation of our TradeRepository. So somewhere we need to indicate the actual TradeRepository implementation class that the service functions need to use. We can define partial applications of each of the above functions for Redis based repository and put them in a separate module ..

object TradeServiceRedisContext extends TradeService {
  val fetchTrade_c = fetchTrade(new TradeRepositoryRedis)
  val updateTrade_c = updateTrade(new TradeRepositoryRedis)
  val writeTrade_c = writeTrade(new TradeRepositoryRedis)
}
*/

package FunctionalStyleDI {

  trait Calculator {
    def calculate(productId: String): Double
  }
  
  class CostPlusCalculator extends Calculator {
    def calculate(productId: String) = {
      0.0
    }
  }

  class ExternalPriceSourceCalculator extends Calculator {
    def calculate(productId: String) = {
      0.0
    }
  }
  
  trait CalculatePriceService {
    def calculate: Calculator => String => Double = {
      calculator => productId => calculator.calculate(productId)
    }     
  } 
  
  object ProductFunctionalSystem extends CalculatePriceService {
    val calculators = Map(
      "costPlus" -> new CostPlusCalculator,
      "externalPriceSource" -> new ExternalPriceSourceCalculator
    )
    def calculate(priceType: String, productId: String): Double = 
      calculate(calculators(priceType))(productId)
  }

  
  object TestFunctionalSystem extends CalculatePriceService {
    val calculators = Map(
      "costPlus" -> new Calculator {
        override def calculate(productId: String) = 0.0
      },
      "externalPriceSource" -> new Calculator {
        override def calculate(productId: String) = 0.0        
      }
    )
    def calculate(priceType: String, productId: String): Double = 
      calculate(calculators(priceType))(productId)
  }
  
   
}
