package chap08.payroll.typeclass.extension

import chap08.payroll.typeclass._

object PayrollSystemWithTypeclassExtension {
  import PayrollSystemWithTypeclass._

  case class JapanPayroll[A](payees: Vector[A])(implicit processor: PayrollProcessor[JapanPayroll, A]) {
    def processPayroll = processor.processPayroll(payees)
  }
  case class Contractor(name: String)
}

object PayrollProcessorsExtension {
  import PayrollSystemWithTypeclassExtension._
  import PayrollSystemWithTypeclass._
  
  implicit object JapanPayrollProcessor extends PayrollProcessor[JapanPayroll, Employee] {
    def processPayroll(payees: Seq[Employee]) = Left("japan employees are processed")
  }
      
  implicit object USContractorPayrollProcessor 
    extends PayrollProcessor[USPayroll, Contractor] {
    def processPayroll(payees: Seq[Contractor]) = Left("us contractors are processed")
  }
  
  implicit object CanadaContractorPayrollProcessor 
    extends PayrollProcessor[CanadaPayroll, Contractor] {
    def processPayroll(payees: Seq[Contractor]) = Left("canada contractors are processed")
  }
  
  implicit object JapanContractorPayrollProcessor 
    extends PayrollProcessor[JapanPayroll, Contractor] {
    def processPayroll(payees: Seq[Contractor]) = Left("japan contractors are processed")
  }
}


object RunNewPayroll {
  import PayrollSystemWithTypeclass._
  import PayrollProcessors._
  import PayrollSystemWithTypeclassExtension._
  import PayrollProcessorsExtension._
  
  def main(args: Array[String]): Unit = run
  def run = {
    val r1 = JapanPayroll(Vector(Employee("a", 1))).processPayroll
    val r2 = JapanPayroll(Vector(Contractor("a"))).processPayroll
    println(r1)    
    println(r2)    
  }
}