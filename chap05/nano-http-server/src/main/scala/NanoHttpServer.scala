import java.net._
import scala.io._
import java.io._
import scala.annotation._
import scala.concurrent._
import ExecutionContext.Implicits.global

object Server {
  import Pure._
  case class IOResource(name: String) extends Resource {
    def exists = new File(name).exists
    def contents = Source.fromFile(name).getLines.toList
    def contentLength = Source.fromFile(name).count(x => true)
  }
  implicit val ioResourceLocator: ResourceLocator = name => IOResource(name)
  
  def main(args: Array[String]) = run(new ServerSocket(8080))
  
  @tailrec private def run(serverSocket: ServerSocket): Unit = {
    handleRequest(serverSocket.accept) 
    run(serverSocket)
  }

  private def handleRequest(client: Socket): Future[_] = Future {
    val response = get(Source.fromInputStream(client.getInputStream))
    val out = new PrintWriter(client.getOutputStream, true)
    out.print(response.mkString(System.getProperty("line.separator")))
    out.close
    client.close
  }    
}

object Pure {
  trait Resource {
    def exists: Boolean
    def contents: List[String]
    def contentLength: Int
  }
  type ResourceLocator = String => Resource
  type Request = Iterator[Char]
  type Response = List[String]
  
  def get(req: Request)(implicit locator: ResourceLocator): Response = {
    val requestedResource = req.takeWhile(x => x != '\n')
                               .mkString.split(" ")(1).drop(1) 
    (_200 orElse _404)(locator(requestedResource))
  }
  
  private def _200: PartialFunction[Resource, Response] = {
    case resource if(resource.exists) => 
            "HTTP/1.1 200 OK" :: 
            ("Date " + new java.util.Date) :: 
            "Content-Type: text/html" :: 
            ("Content-Length: " + resource.contentLength) ::
            System.getProperty("line.separator") ::
            resource.contents
  }

  private def _404: PartialFunction[Resource, Response] = { case _ => List("HTTP/1.1 404 Not Found") }
}
