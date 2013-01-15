// Start scala and run via> :load CustomCollections.scala
package chap04

import java.util.{Collection => JCollection, ArrayList }

class JavaToTraversable[E](javaCollection: JCollection[E]) extends Traversable[E] {

  def foreach[U](f : E => U): Unit = {
    val iterator = javaCollection.iterator
    while(iterator.hasNext) {
      println("inside foreach")
      f(iterator.next)
    }
  }
}

object JavaToTraversableExample extends App {
  val jCol1 = new ArrayList[Int]
  jCol1.add(100)
  jCol1.add(200)
  jCol1.add(300)

  val t = new JavaToTraversable(jCol1)
  println(t map { _ + 1 })  
}
