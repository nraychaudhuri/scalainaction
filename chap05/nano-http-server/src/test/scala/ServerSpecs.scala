import org.specs2.mutable._
import scala.io._
import Pure._
class ServerSpecs extends Specification {
  "Light Server" should {
    "return the contents of available resource" in {
      val response = get("GET /test.txt HTTP/1.0\n".toIterator)(name => Server.IOResource(name))

      response(0) must beEqualTo("HTTP/1.1 200 OK") 
      response(1) must beEqualTo("Date " + new java.util.Date) 
      response(2) must beEqualTo("Content-Type: text/html") 
      response(3) must beEqualTo("Content-Length: " + Source.fromFile("test.txt").count(x => true)) 
	  response(4) must beEqualTo(System.getProperty("line.separator"))
      response(5) must beEqualTo("<html>")
      response(6) must beEqualTo("<body>")
      response(7) must beEqualTo("This is a sample response")
      response(8) must beEqualTo("</body>")
      response(9) must beEqualTo("</html>")
    }
    
    "return 404 when not found" in {
      val response = get("GET /junk.txt HTTP/1.0\n".toIterator)(name => Server.IOResource(name))
      response(0) must beEqualTo("HTTP/1.1 404 Not Found")       
    }
  }
}