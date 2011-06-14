package com.akkaoogle.infrastructure

import com.akkaoogle.calculators.pricecalculators._
import akka.actor.Actor.remote
import akka.actor.Actor._
import com.akkaoogle.db.models._
import akka.actor.{Supervisor, ActorRef, Actor}
import akka.config.Supervision._
import com.akkaoogle.infrastructure.dispatchers._

object RemoteActorServer {

  def run() {
    remote.start("localhost", 3552)
    register()
  }

  def register() {
    val monitor = actorOf[MonitorActor]

    val cheapestDealFinders = cheapestDealFinderActors(10)
    val cheapestDealFinderLoadBalancer =
       actorOf(new CheapestDealFinderLoadBalancer(cheapestDealFinders))
    val cheapestDealFinderSupervisorConfig = SupervisorConfig(
        OneForOneStrategy(List(classOf[Exception]), 3, 1000),
        for(w <- cheapestDealFinders) yield Supervise(w, Permanent, false)
    )

    val internalPriceCalculators: List[ActorRef] = createInternalPriceCalculators(10)
    val internalLoadBalancer =
      actorOf(new InternalPriceCalculatorLoadBalancer(internalPriceCalculators))

    val internalChildSupervisorConfig = SupervisorConfig(
        OneForOneStrategy(List(classOf[Exception]), 3, 1000),
        for(w <- internalPriceCalculators) yield Supervise(w, Permanent, false)
    )

    val proxies = createExternalProxyActors(ExternalVendor.findAll)
    val externalPriceCalculators: List[ActorRef] = createExternalPriceCalculators(10, proxies)
    val externalLoadBalancer =
      actorOf(new ExternalPriceCalculatorLoadBalancer(externalPriceCalculators))


    val externalProxySupervisor = SupervisorConfig(
          OneForOneStrategy(List(classOf[Exception]), 3, 1000),
          for(w <- proxies) yield Supervise(w, Permanent, false)
        )

    val externalChildSupervisorConfig = SupervisorConfig(
        OneForOneStrategy(List(classOf[Exception]), 3, 1000),
        (for(w <- externalPriceCalculators) yield Supervise(w, Permanent, false))
        ::: externalProxySupervisor
        :: Nil
    )

    val topSupervisor = Supervisor(
       SupervisorConfig(
          AllForOneStrategy(List(classOf[Exception]), 3, 1000),
          Supervise(cheapestDealFinderLoadBalancer,
            Permanent,
            true
          )
          :: Supervise(
            internalLoadBalancer,
            Permanent,
            true)
          :: Supervise(
            externalLoadBalancer,
            Permanent,
            true)
          :: Supervise(
            monitor,
            Permanent,
            true
          )
          :: cheapestDealFinderSupervisorConfig
          :: internalChildSupervisorConfig
          :: externalChildSupervisorConfig
          :: Nil)
    )

  }

  def lookup(name: String): ActorRef = remote.actorFor(name, 5000L, "localhost", 3552)

  def stop() {
    remote.shutdown
  }

  private def createExternalProxyActors(vendors: Iterable[ExternalVendor]) = {
    val proxies = for(v <- vendors) yield  {
       val ref = actorOf(new ExternalVendorProxyActor(v))
       remote.register("externalVendor" + v.id, ref)
       ref
    }
    proxies.toList
  }

  private def cheapestDealFinderActors(initialLoad: Int) = {
    (for (i <- 0 until initialLoad) yield Actor.actorOf[CheapestDealFinder]).toList
  }

  private def createInternalPriceCalculators(initialLoad: Int) = {
    (for (i <- 0 until initialLoad) yield Actor.actorOf[InternalPriceCalculator]).toList
  }

  private def createExternalPriceCalculators(initialLoad: Int, proxies: List[ActorRef]) = {
    (for (i <- 0 until initialLoad) yield Actor.actorOf(new ExternalPriceCalculator(proxies))).toList
  }
}