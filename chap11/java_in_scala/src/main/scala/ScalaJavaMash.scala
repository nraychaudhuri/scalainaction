package chap11.scala

import chap11.java._
import java.util.Date

class PaymentCalculator(val payPerDay: Int = 100) extends DateCalculator {
	def calculatePayment(start: Date, end: Date) = {
		daysBetween(start, end) * payPerDay
	}
	
	def chronologyUsed = 	DateCalculator.getChronologyUsed
}

object Main {
  def write(content: String): Either[Exception, Boolean] = {
    val w = new Writer
    try {
      w.writeToFile(content)
      Right(true)      
    }catch {
      case e: java.io.IOException => Left(e)
    }
  }
  
  def main(args: Array[String]): Unit = run

	def run = {
	  val w = new Writer
	  w.writeToFile("This is a test")
	  
		val x = new PaymentCalculator
		val startDate = new java.text.SimpleDateFormat("MM-dd-yyyy").parse("10-01-2010");
		val endDate = new java.text.SimpleDateFormat("MM-dd-yyyy").parse("10-15-2010");
		println(x.calculatePayment(startDate, endDate))
	}
}
