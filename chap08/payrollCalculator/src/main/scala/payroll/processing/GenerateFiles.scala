package payroll.processing

object Generator {
  def main(args: Array[String]) {
    import java.io._  
    ('a' to 'z') foreach { i => 
      val f = new FileWriter("src/main/resources/" + i.toString + "_company.txt")
      val p = new PrintWriter(f)
      (1 to 10000) foreach {i =>  p.println("10 20 30 40 50 60 70") }   
      p.close; f.close
    }
  }
}