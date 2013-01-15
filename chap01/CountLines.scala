// Run with >scala CountLines.scala  or
// run with the REPL in chap01/ via
// scala> :load CountLines.scala

object CountLines extends App {
  import System._
  val src = scala.io.Source.fromFile("chap01/someFile.txt")
  val count = src.getLines().map(x => 1).sum
  println(count)  
  
}
