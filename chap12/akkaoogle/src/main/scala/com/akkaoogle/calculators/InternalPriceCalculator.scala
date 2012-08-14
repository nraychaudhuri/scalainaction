package com.akkaoogle.calculators

import messages._
import com.akkaoogle.db.models._
import akka.actor._

class InternalPriceCalculator extends Actor {
  def receive = {
    case FindPrice(productDescription, quantity) =>
      val price = calculatePrice(productDescription, quantity)
      sender ! price
  }

  def calculatePrice(productDescription: String, qty: Int): Option[LowestPrice] = {
    Product.findByDescription(productDescription) map { product =>
      Some(LowestPrice(product.vendorName,
        product.description,
        product.calculatePrice * qty))
    } getOrElse Option.empty[LowestPrice]
  }
}
