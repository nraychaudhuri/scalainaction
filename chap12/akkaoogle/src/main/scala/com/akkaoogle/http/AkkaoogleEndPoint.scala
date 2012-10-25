/*
import akka.actor._
import com.akkaoogle.db.models._
import com.akkaoogle.infrastructure.AkkaoogleActorServer
import akka.http._
import com.akkaoogle.calculators.messages.{LowestPrice, FindPrice, FindStats, Stats}
import akka.pattern.{ask, pipe}

class AkkaoogleEndPoint extends Actor with Endpoint {
  //self.dispatcher = Endpoint.Dispatcher

  final val AdminRoot = "/akkaoogle/admin/"
  final val Monitor = AdminRoot + "monitor"
  final val AppRoot = "/akkaoogle/"
  final val Search = AppRoot + "search"
  def hook(uri:String) = uri == Monitor || uri == Search
  def provide(uri:String): ActorRef =
    uri match {
      case Monitor =>
        context.actorOf(Props[AdminMonitorClientActor])
      case Search =>
        context.actorOf(Props[SearchClientActor])
    }

  override def preStart = {
    //Actor.registry.actorsFor(classOf[RootEndpoint]).head ! Endpoint.Attach(hook, provide)
  }
 
  def receive = handleHttpRequest

}

class SearchClientActor extends Actor {
  def receive = {
    case searchProduct: Post =>
      val desc = searchProduct.getParameterOrElse("productDescription", x => "")
      val result =
        (AkkaoogleActorServer.lookup("cheapest-deal-finder") ? FindPrice(desc, 1)).mapTo[Option[LowestPrice]]
      result onSuccess {
	          case Some(lowestPrice)=> searchProduct OK renderResult(lowestPrice)
	    }
  }

  private def renderResult[T](result: T) = {
    val html = result match {
      case LowestPrice(vendorName, productDescription, price) =>
        <div>
          <h2>The lowest price for {productDescription}
            found from {vendorName} is <b>{price}</b></h2>
        </div>
      case _ => <h2>No price found</h2>

    }
    html.toString
  }
}

class AdminMonitorClientActor extends Actor
{
  def receive =
  {
    case get:Get =>
      val logs = retrieveLogs(ExternalVendor.findAll)
      get OK renderLog(logs)
    case other:RequestMethod => other NotAllowed "unsupported request"
  }

  private def retrieveLogs(vendors: Iterable[ExternalVendor]) = {
    // val monitor = AkkaoogleActorServer.lookup("monitor")
    // for(v <- vendors) yield {
    //   val resultOption = monitor !! FindStats(v.name)
    //   resultOption.getOrElse(Stats(v.name, 0)).asInstanceOf[Stats]
    // }
   null
  }

  private def renderLog(logs: Iterable[Stats]) = {
    <html>
      <head>
        <title>Transaction Log</title>
      </head>
      <body>
        <h1> Timeouts </h1>
        <table>
          {for(l <- logs) yield row(l)}
        </table>
      </body>
    </html>.toString
  }

  private def row(s: Stats) {
    <tr>
      <td>{s.actorId}</td>
      <td>{s.timeouts}</td>
    </tr>
  }

}

*/

