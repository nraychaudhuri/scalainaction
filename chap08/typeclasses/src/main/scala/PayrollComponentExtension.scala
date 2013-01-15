package chap08.payroll.extension

import chap08.payroll._

trait JapanPayrollSystem extends PayrollSystem {
  class JapanPayroll extends Payroll {
    def processEmployees(employees: Vector[Employee]) = Left("Japan payroll")
  }
}

trait ContractorPayrollSystem extends PayrollSystem {
  type P <: Payroll
  case class Contractor(name: String)

  trait Payroll extends super.Payroll {
    def processContractors(contractors: Vector[Contractor]): Either[String, Throwable]
  }
}

trait USContractorPayrollSystem extends USPayrollSystem with ContractorPayrollSystem {
  class USPayroll extends super.USPayroll with Payroll {
    def processContractors(contractors: Vector[Contractor]) = Left("US contract payroll")
  }
}

trait CanadaContractorPayrollSystem extends CanadaPayrollSystem with ContractorPayrollSystem {
  class CanadaPayroll extends super.CanadaPayroll with Payroll {
    def processContractors(contractors: Vector[Contractor]) = Left("Canada contract payroll")
  }
}

trait JapanContractorPayrollSystem extends JapanPayrollSystem with ContractorPayrollSystem {
  class JapanPayroll extends super.JapanPayroll with Payroll {
    def processContractors(contractors: Vector[Contractor]) = Left("Japan contract payroll")
  }
}

object RunNewPayroll {
  object USNewPayrollInstance extends USContractorPayrollSystem {
    type P = USPayroll
    def processPayroll(p: USPayroll) = {
      p.processEmployees(Vector(Employee("a", 1)))
      p.processContractors(Vector(Contractor("b")))
      Left("payroll processed successfully")
    }
  }
  import USNewPayrollInstance._   
  def main(args: Array[String]): Unit = run
  def run = {
    val usPayroll = new USPayroll
    processPayroll(usPayroll)
  }
}