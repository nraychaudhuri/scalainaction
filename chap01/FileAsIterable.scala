// Run with >scala FileAsIterable.scala  or
// run with the REPL in chap01/ via
// scala> :load FileAsIterable.scala


class FileAsIterable {
  def iterator = scala.io.Source.fromFile("chap01/someFile.txt").getLines()
}

object FileAsIterableExample extends App {
  val newIterator = new FileAsIterable with Iterable[String]
  newIterator.foreach { line => println(line) }
}
