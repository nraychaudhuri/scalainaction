package com.scalainaction.mongo

import com.mongodb._

class MongoClient(val host:String, val port:Int) {
  require(host != null, "You have to provide a host name")
  private val underlying = new Mongo(host, port)
  def this() = this("127.0.0.1", 27017)

  def version = underlying.getVersion
  def dropDB(name:String) = underlying.dropDatabase(name)
  def createDB(name:String) = DB(underlying.getDB(name))
  def db(name:String) = DB(underlying.getDB(name))
}

