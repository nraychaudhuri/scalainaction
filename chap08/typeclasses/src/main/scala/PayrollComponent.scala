package chap08.payroll

trait PayrollSystem {
  case class Employee(name: String, id: Long) 
  type P <: Payroll
  trait Payroll {
    def processEmployees(employees: Vector[Employee]): Either[String, Throwable]
  }
  def processPayroll(p: P): Either[String, Throwable]
}

trait USPayrollSystem extends PayrollSystem {
  class USPayroll extends Payroll {
    def processEmployees(employees: Vector[Employee]) = Left("US payroll")
  }
} 

trait CanadaPayrollSystem extends PayrollSystem {
  class CanadaPayroll extends Payroll {
    def processEmployees(employees: Vector[Employee]) = Left("Canada payroll")
  }
}

object RunPayroll1 {
  object USPayrollInstance extends USPayrollSystem {
    type P = USPayroll
    def processPayroll(p: USPayroll) = {
      val result = p.processEmployees(Vector(Employee("a", 1)))
      println(result)
      result
    }
  }
  import USPayrollInstance._
  
  def main(args: Array[String]): Unit = run
  def run = {
    val usPayroll = new USPayroll
    processPayroll(usPayroll)
  }
}
