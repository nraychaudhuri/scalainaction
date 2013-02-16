package com.akkaoogle.infrastructure

import com.akkaoogle.calculators._
import akka.actor._
import com.akkaoogle.db.models._
import akka.actor.{ActorRef, Actor}
import akka.routing._
import com.typesafe.config.ConfigFactory

object AkkaoogleActorServer {

  var system: Option[ActorSystem] = None

  def run(): Unit = {
	  println("starting the actor server...")
	  system = Some(ActorSystem(name = "akkaoogle", 
      config = ConfigFactory.load.getConfig("akkaoogle")))
    system.foreach(s => register(s))
  }

  private def register(implicit system: ActorSystem) {
    val monitor = system.actorOf(Props[MonitorActor], name = "monitor")

    val cheapestDealFinderLoadBalancer = system.actorOf(
							Props[CheapestDealFinder].withRouter(SmallestMailboxRouter(nrOfInstances = 10)), name = "cheapest-deal-finder-balancer")   

    val internalPriceCalculators: List[ActorRef] = createInternalPriceCalculators(10)
 
    val internalLoadBalancer = system.actorOf(
    	Props[InternalPriceCalculator].withRouter(RoundRobinRouter(routees = internalPriceCalculators)), name = "internal-load-balancer")    

    val proxies = createExternalProxyActors(ExternalVendor.findAll)
    val externalPriceCalculators: List[ActorRef] = createExternalPriceCalculators(10, proxies)
    val externalLoadBalancer = system.actorOf(
    	Props[ExternalPriceCalculator].withRouter(RoundRobinRouter(routees = externalPriceCalculators)), name="external-load-balancer")    
  }

  def lookup(name: String): ActorRef = { 
	  system map { s =>
		  val path = s / name
		  s.actorFor(path) 
	  } getOrElse(throw new RuntimeException("No actor found")) 
	}

  def stop() {
	  system.foreach(_.shutdown())
  }

  private def createExternalProxyActors(vendors: Iterable[ExternalVendor])(implicit system: ActorSystem) = {
    val proxies = for(v <- vendors) yield  {
	     println(s"Creating vendor proxies for ${v.name}")
	     val ref = system.actorOf(Props(new ExternalVendorProxyActor(v))
										.withDispatcher("dispatchers.proxy-actor-dispatcher"), name=v.name)
       ref
    }
    proxies.toList
  }

  private def createInternalPriceCalculators(initialLoad: Int)(implicit system: ActorSystem) = {
    (for (i <- 0 until initialLoad) yield system.actorOf(Props[InternalPriceCalculator]
						.withDispatcher("dispatchers.internal-price-calculator-actor-dispatcher"), 
						name=("internal-price-calculator" + i))).toList
  }

  private def createExternalPriceCalculators(initialLoad: Int, proxies: List[ActorRef])(implicit system: ActorSystem) = {
    (for (i <- 0 until initialLoad) yield system.actorOf(
								Props(new ExternalPriceCalculator(proxies))
									.withDispatcher("dispatchers.external-price-calculator-actor-dispatcher"), 
									name = ("external-price-calculator" + i))).toList
  }
}