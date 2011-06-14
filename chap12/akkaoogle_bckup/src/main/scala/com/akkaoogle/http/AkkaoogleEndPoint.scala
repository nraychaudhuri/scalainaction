package com.akkaoogle.http

import akka.actor.{ActorRef, Actor}
import com.akkaoogle.db.models._
import com.akkaoogle.infrastructure.RemoteActorServer
import akka.http._
import com.akkaoogle.calculators.messages.{LowestPrice, FindPrice, FindStats, Stats}

class AkkaoogleEndPoint extends Actor with Endpoint {
  self.dispatcher = Endpoint.Dispatcher

  final val AdminRoot = "/akkaoogle/admin/"
  final val Monitor = AdminRoot + "monitor"
  final val AppRoot = "/akkaoogle/"
  final val Search = AppRoot + "search"
  def hook(uri:String) = uri == Monitor || uri == Search
  def provide(uri:String): ActorRef =
    uri match {
      case Monitor =>
        Actor.actorOf[AdminMonitorClientActor].start
      case Search =>
        Actor.actorOf[SearchClientActor].start
    }

  override def preStart =
    Actor.registry.actorsFor(classOf[RootEndpoint]).head ! Endpoint.Attach(hook, provide)

  def receive = handleHttpRequest

}

class SearchClientActor extends Actor {
  def receive = {
    case searchProduct: Post =>
      val desc = searchProduct.getParameterOrElse("productDescription", x => "")
      val resultOption =
        RemoteActorServer.lookup("cheapest-deal-finder") !! FindPrice(desc, 1)
      searchProduct OK renderResult(resultOption)
  }

  private def renderResult[T](resultOption: Option[T]) = {
    val html = resultOption match {
      case Some(Some(LowestPrice(vendorName, productDescription, price))) =>
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
    val monitor = RemoteActorServer.lookup("monitor")
    for(v <- vendors) yield {
      val resultOption = monitor !! FindStats(v.name)
      resultOption.getOrElse(Stats(v.name, 0)).asInstanceOf[Stats]
    }
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



