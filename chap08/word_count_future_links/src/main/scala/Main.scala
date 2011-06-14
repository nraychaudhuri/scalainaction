object Main {
  def main(args: Array[String]) {
    val m = new WordCountMaster
    m.start
    m !? StartCounting("src/main/resources/", 2) match {
      case FinishedCounting(result) =>  println("final result " + result)
    }
  }
}
