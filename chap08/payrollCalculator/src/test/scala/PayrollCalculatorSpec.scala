import org.specs._

import payroll.processing._
import payroll.processing.messages._
import scala.actors.Futures._

class PayrollCalculatorSpec extends Specification {

  "Payroll calculator" should {
    "load payroll file" in {   
      val calculator = new PayrollCalculator()
      def response = calculator !! FileLoad("src/test/resources/payroll_file.txt")
      def errorHandler = future { Thread.sleep(100); Result(0) }
      awaitEither(response, errorHandler) mustEqual Result(3, 0)
    }
    
    "send each record to worker actor" in {
      val calculator = new PayrollCalculator()
      def testWorkers = {
        
      }
      calculator.workers({def createWorkers = testWorkers })
      calculator ! FileLoad("src/test/resources/payroll_file.txt")
    }
  } 
}