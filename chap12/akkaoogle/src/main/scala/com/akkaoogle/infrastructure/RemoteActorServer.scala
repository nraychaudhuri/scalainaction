package com.akkaoogle.infrastructure

import com.akkaoogle.calculators._
import akka.actor._
import com.akkaoogle.db.models._
import akka.actor.{ActorRef, Actor}
import akka.remote.RemoteScope
import akka.routing._

object RemoteActorServer {

  var system: Option[ActorSystem] = None

  def run(): Unit = {
	  println("starting the remote server...")
	  system = Some(ActorSystem("akkaoogle"))
    system.foreach(s => register(s))
  }

  private def register(implicit system: ActorSystem) {
    val monitor = system.actorOf(Props[MonitorActor], name = "monitor")

    val cheapestDealFinders = cheapestDealFinderActors(10)
 
    val cheapestDealFinderLoadBalancer = system.actorOf(
							Props[CheapestDealFinder].withRouter(SmallestMailboxRouter(routees = cheapestDealFinders)), name = "cheapest-deal-finder-balancer")   

    val internalPriceCalculators: List[ActorRef] = createInternalPriceCalculators(10)
 
    val internalLoadBalancer = system.actorOf(
    	Props[InternalPriceCalculator].withRouter(RoundRobinRouter(routees = internalPriceCalculators)), name = "internal-load-balancer")    

    val proxies = createExternalProxyActors(ExternalVendor.findAll)
    val externalPriceCalculators: List[ActorRef] = createExternalPriceCalculators(10, proxies)
    val externalLoadBalancer = system.actorOf(
    	Props[ExternalPriceCalculator].withRouter(RoundRobinRouter(routees = externalPriceCalculators)), name="external-load-balancer")    
    
    // val externalLoadBalancer =
    //   system.actorOf(Props(new ExternalPriceCalculatorLoadBalancer(externalPriceCalculators)), name = "external-load-balancer")

	    // val cheapestDealFinderSupervisorConfig = SupervisorConfig(
	    //     OneForOneStrategy(List(classOf[Exception]), 3, 1000),
	    //     for(w <- cheapestDealFinders) yield Supervise(w, Permanent, false)
	    // )


	    // val internalChildSupervisorConfig = SupervisorConfig(
	    //     OneForOneStrategy(List(classOf[Exception]), 3, 1000),
	    //     for(w <- internalPriceCalculators) yield Supervise(w, Permanent, false)
	    // )


    // val externalProxySupervisor = SupervisorConfig(
    //       OneForOneStrategy(List(classOf[Exception]), 3, 1000),
    //       for(w <- proxies) yield Supervise(w, Permanent, false)
    //     )
    // 
    // val externalChildSupervisorConfig = SupervisorConfig(
    //     OneForOneStrategy(List(classOf[Exception]), 3, 1000),
    //     (for(w <- externalPriceCalculators) yield Supervise(w, Permanent, false))
    //     ::: externalProxySupervisor
    //     :: Nil
    // )
    // 
    // val topSupervisor = Supervisor(
    //    SupervisorConfig(
    //       AllForOneStrategy(List(classOf[Exception]), 3, 1000),
    //       Supervise(cheapestDealFinderLoadBalancer,
    //         Permanent,
    //         true
    //       )
    //       :: Supervise(
    //         internalLoadBalancer,
    //         Permanent,
    //         true)
    //       :: Supervise(
    //         externalLoadBalancer,
    //         Permanent,
    //         true)
    //       :: Supervise(
    //         monitor,
    //         Permanent,
    //         true
    //       )
    //       :: cheapestDealFinderSupervisorConfig
    //       :: internalChildSupervisorConfig
    //       :: externalChildSupervisorConfig
    //       :: Nil)
    // )

  }

  def lookup(name: String): ActorRef = { 
	  system map { s =>
		  val path = s / name
		  s.actorFor(path) 
	  } getOrElse(throw new RuntimeException("No actor found")) 
		//remote.actorFor(name, 5000L, "localhost", 3552)
	}

  def stop() {
	  system.foreach(_.shutdown())
  }

  private def createExternalProxyActors(vendors: Iterable[ExternalVendor])(implicit system: ActorSystem) = {
  	val address = Address("akka", "sys", "localhost", 3552)
    val proxies = for(v <- vendors) yield  {
	     println("Creating vendor proxies for " + v.name)
	     val ref = system.actorOf(Props(new ExternalVendorProxyActor(v))
										.withDispatcher("external-vendor-proxy-actor-dispatcher")
										.withDeploy(Deploy(scope = RemoteScope(address))), name = v.name)
       ref
    }
    proxies.toList
  }

  private def cheapestDealFinderActors(initialLoad: Int)(implicit system: ActorSystem) = {
    (for (i <- 0 until initialLoad) yield system.actorOf(Props[CheapestDealFinder], name=("cheapest-deal-finder" + i))).toList
  }

  private def createInternalPriceCalculators(initialLoad: Int)(implicit system: ActorSystem) = {
    (for (i <- 0 until initialLoad) yield system.actorOf(Props[InternalPriceCalculator], name=("internal-price-calculator" + i))).toList
  }

  private def createExternalPriceCalculators(initialLoad: Int, proxies: List[ActorRef])(implicit system: ActorSystem) = {
    (for (i <- 0 until initialLoad) yield system.actorOf(Props(new ExternalPriceCalculator(proxies)))).toList
  }
}