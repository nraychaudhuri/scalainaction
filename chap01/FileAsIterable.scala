import java.io.File
import scala.io.Source 
import System._
class FileAsIterable {
  val src = Source.fromFile(new File("someFile.txt"))
  def iterator = src.getLines()
}

val newIterator = new FileAsIterable with Iterable[String]
newIterator.foreach { line => println(line) }
