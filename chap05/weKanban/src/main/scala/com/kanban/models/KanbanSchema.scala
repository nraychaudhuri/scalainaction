package com.kanban.models

import org.squeryl._
import org.squeryl.adapters._
import org.squeryl.PrimitiveTypeMode._
import java.sql.DriverManager

object KanbanSchema extends Schema {
  val stories = table[Story]("STORIES")
  
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
  
  def main(args: Array[String]) {
    println("initializing the weKanban schema")
    init
    inTransaction { drop ; create }      
  }
  
}
