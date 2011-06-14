object Main {
  def main(args: Array[String]) {
    val m = new WordCountMaster("src/main/resources/", 2)
    m.beginCounting
    m.waitUntilDone
    println("Done with work " + m.sortedCount)
  }
}
