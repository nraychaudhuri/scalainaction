// Start the REPL and load 
// > :load RichConsole.scala
// > :load Dates.scala

import java.util.Date
import java.sql.{Date => SqlDate}
import RichConsole._

object DateExample extends App {
  val now = new Date
  p(now)
  val sqlDate = new SqlDate(now.getTime)
  p(sqlDate)  
}
