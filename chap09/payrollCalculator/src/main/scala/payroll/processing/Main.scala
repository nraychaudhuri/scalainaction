package payroll.processing

object Main {
  import messages._
  def main(args: Array[String]) {
    new PayrollCalculator() ! Start("src/main/resources/")
  }
}