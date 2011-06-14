package com.akkaoogle.db

import org.squeryl._
import org.squeryl.adapters._
import org.squeryl.PrimitiveTypeMode._
import java.sql.DriverManager
import com.akkaoogle.db.models._

object AkkaoogleSchema extends Schema {
  val products = table[Product]("PRODUCTS")
  val vendors = table[ExternalVendor]("VENDORS")
  val transactionFailures = table[TransactionFailure]("TRANSACTION_LOG")

  def init = {
    import org.squeryl.SessionFactory
    Class.forName("org.h2.Driver")
    if(SessionFactory.concreteFactory.isEmpty) {
      SessionFactory.concreteFactory = Some(()=>
        Session.create(
          DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", ""),
          new H2Adapter))
    }
  }

  def tx[A](a: =>A): A = {
    init
    inTransaction(a)
  }

  def createSchema() {
    tx { drop ; create }
  }

}