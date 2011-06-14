import java.io.File
import scala.io.Source
import System._

val src = Source.fromFile(new File("someFile.txt"))
val count = src.getLines(getProperty("line.separator")).foldLeft(0) { 
	(i, line) => i + 1 
}
println(count)