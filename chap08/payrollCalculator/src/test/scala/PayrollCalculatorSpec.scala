import org.specs2.mutable._

import payroll.processing._
import payroll.processing.messages._
import scala.actors.Futures._

import java.io._
import scala.io._
class PayrollCalculatorSpec extends Specification {
	val testFile = getClass.getResource("/payroll_file.txt").toURI().getPath() // necessary on windows with spaces in path

  "Payroll calculator" should {
    "load payroll file" in {   
      val calculator = new CompanyPayrollActor()
      val response = calculator !! FileLoad(testFile)

      def errorHandler = future { Thread.sleep(100); "Failed" }
      awaitEither(response, errorHandler) mustEqual "OK"
    }
    
    "send each record to worker actor" in {
     val responseList = Source.fromFile(testFile).getLines.map {line => 
      new PayrollActor() !! ProcessPayroll(line)
     }.toList
     
     def errorHandler = future { Thread.sleep(100); Result(0, 1) }
     
     awaitEither(responseList(0), errorHandler) mustEqual Result(150, 0)
     awaitEither(responseList(1), errorHandler) mustEqual Result(33, 0)
     awaitEither(responseList(2), errorHandler) mustEqual Result(7, 0)
    }
  } 
}