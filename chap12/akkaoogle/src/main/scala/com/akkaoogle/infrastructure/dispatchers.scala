package com.akkaoogle.infrastructure

import akka.routing.{LoadBalancer, CyclicIterator, SmallestMailboxFirstIterator}
import akka.actor.{Actor, ActorRef}
import akka.routing._
import akka.dispatch.Dispatchers

object dispatchers {

  object ExternalVendorProxyActor {
    val dispatcher = Dispatchers
                     .newExecutorBasedEventDrivenDispatcher("proxy dispatcher")
                     .withNewThreadPoolWithLinkedBlockingQueueWithCapacity(100)
                     .setCorePoolSize(3)
                     .setMaxPoolSize(100).build
  }

  class CheapestDealFinderLoadBalancer(workers: List[ActorRef]) extends Actor with LoadBalancer {
    self.id = "cheapest-deal-finder"
    self.timeout = 200L
    val seq = new SmallestMailboxFirstIterator(workers)
  }

  class InternalPriceCalculatorLoadBalancer(workers: List[ActorRef]) extends Actor with LoadBalancer {
    self.id = "internal-load-balancer"
    self.timeout = 200L
    val seq = new CyclicIterator[ActorRef](workers)
  }

  class ExternalPriceCalculatorLoadBalancer(workers: List[ActorRef]) extends Actor with LoadBalancer {
    self.id = "external-load-balancer"
    self.timeout = 200L
    val seq = new CyclicIterator[ActorRef](workers)
  }

  object InternalPriceCalculatorActor {
    val dispatcher = Dispatchers
      .newExecutorBasedEventDrivenWorkStealingDispatcher("internal calculator dispatcher")
      .withNewThreadPoolWithLinkedBlockingQueueWithUnboundedCapacity
      .setCorePoolSize(5)
      .build
  }

}