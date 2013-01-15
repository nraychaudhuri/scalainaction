// Start scala >scala
// load into the REPL> :load ViewExample.scala

package chap04
import scala.io._
import scala.xml.XML

object ViewExample extends App {
	def tweets(handle: String) = {
	  println("processing tweets for " + handle)
	  val source = Source.fromURL(new java.net.URL("http://search.twitter.com/search.atom?q=" + handle))   
	  val iterator = source.getLines()
	  val builder = new StringBuilder
	  for(line <- iterator) builder.append(line)
	  XML.loadString(builder.toString)
	}

	val allTweets = Map("nraychaudhuri" -> tweets _ , "ManningBooks" -> tweets _, "bubbl_scala" -> tweets _)

	for(t <- allTweets; if(t._1 == "ManningBooks")) t._2(t._1)	
}

