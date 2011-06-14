weKanban: A web based Kanban application built using Scalaz
================

This is an example project from the book [Scala in Action][scala_in_action].

This project is build using Scalaz, Scala 2.8 and SBT.

How to Start?
===============

* Download all the dependencies using "sbt update"
* Start the H2 database server using "sbt h2-start" action
* Start the Jetty server using "sbt jetty" action
* And launch the app by going to http://localhost:8080/kanban/board
 
In the current version of weKanban you can create new cards, move them between phases through drag and drop. In
future I will add more features to this.


[scala_in_action]: http://www.manning.com/raychaudhuri/
