package chap08.payroll.typeclass

import scala.language.higherKinds

object PayrollSystemWithTypeclass {
  case class Employee(name: String, id: Long)
  
  trait PayrollProcessor[C[_], A] {
    def processPayroll(payees: Seq[A]): Either[String, Throwable]
  } 
  
  case class USPayroll[A](payees: Seq[A])(implicit processor: PayrollProcessor[USPayroll, A]) {
    def processPayroll = processor.processPayroll(payees)
  } 
  case class CanadaPayroll[A](payees: Seq[A])(implicit processor: PayrollProcessor[CanadaPayroll, A]) {
    def processPayroll = processor.processPayroll(payees)
  }
}

object PayrollProcessors {
  import PayrollSystemWithTypeclass._
  implicit object USPayrollProcessor extends PayrollProcessor[USPayroll, Employee] {
    def processPayroll(payees: Seq[Employee]) = Left("us employees are processed")
  }
  
  implicit object CanadaPayrollProcessor extends PayrollProcessor[CanadaPayroll, Employee] {
    def processPayroll(payees: Seq[Employee]) = Left("canada employees are processed")
  }  
}

object RunPayroll2 {
  import PayrollSystemWithTypeclass._
  import PayrollProcessors._
  
  def main(args: Array[String]): Unit = run
  def run = {
    val r = USPayroll(Vector(Employee("a", 1))).processPayroll
    println(r)    
  }
}

