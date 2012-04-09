package payroll.processing

import scala.actors._
import scala.actors.Actor._
import scala.io._
import java.io._

package messages {
  case class Start(docRoot: String)
  case class FileLoad(filename: String)
  case class ProcessPayroll(data: String)
  case class Payroll(pay: Double, tax: Double)
  case class Result(success: Int, error: Int = 0)
}

class PayrollCalculator extends Actor {
  import messages._
  start()
  def act = {
      receive {
        case Start(docRoot) => {
          val f = new File(docRoot)
          
          new File(docRoot).list.map(docRoot + _).foreach {file =>
            new CompanyPayrollActor() ! FileLoad(file)
          }
          }
      }
  }  
}

class CompanyPayrollActor extends Actor {
  import messages._
  start()
  def act = {
    receive {
      case FileLoad(filename) => 
        println("processing the file " + filename)
        Source.fromFile(new File(filename)).getLines.foreach {line => 
          new PayrollActor() ! ProcessPayroll(line) 
        }
        
        reply("OK")
    }    
  }
}

class PayrollActor extends Actor {
  import messages._
  start()
  def act = {
    react {
      case ProcessPayroll(data) =>
        val sum = data.split(" ").map(_.toInt).foldLeft(0){_ + _}
        println(">>>>>> current time " + System.nanoTime)
        reply(Result(sum, 0))
    }
  }
}




