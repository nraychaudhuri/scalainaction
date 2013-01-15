import java.util.Date

object structuraltype extends App {
  trait SalariedWorker {
    def salary: BigDecimal
  }
  trait Worker extends SalariedWorker {
    def bonusPercentage: Double 
  }

  trait HourlyWorker extends SalariedWorker {
    def hours: Int
  }

  case class FullTimeWorker(val salary: BigDecimal, val bonusPercentage: Double) extends Worker
  case class PartTimeWorker(val hours: Int, val salary: BigDecimal) extends HourlyWorker
  case class StudentWorker(val hours: Int, val salary: BigDecimal) extends HourlyWorker

  def amountPaidAsSalary(workers: Vector[SalariedWorker]) = {
  }  
  
  def amountPaidAsSalary2(workers: Vector[{def salary: BigDecimal }]) = {
  }
  
  type Profile = {
    def name: String
    def dob: Date
  }
  
  def run {
    amountPaidAsSalary2(Vector(PartTimeWorker(5, 4000.50), StudentWorker(2, 100.20)))     
  }

  run
}

