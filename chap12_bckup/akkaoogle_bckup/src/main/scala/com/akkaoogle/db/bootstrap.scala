package com.akkaoogle.db

import akka.actor._
import AkkaoogleSchema._

object bootStrap {

  def main(args: Array[String]) {
    println("initializing the Pricakka schema")
    createSchema()
  }

}