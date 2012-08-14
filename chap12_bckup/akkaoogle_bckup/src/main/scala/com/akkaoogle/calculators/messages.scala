package com.akkaoogle.calculators

object messages {
  case class FindPrice(productDescription: String, quantity: Int)
  case class LowestPrice(vendorName: String, productDescription: String, price: Double)

  case class LogTimeout(actorId: String, msg: String)

  case class FindStats(actorId: String)
  case class Stats(actorId: String, timeouts: Int)
}