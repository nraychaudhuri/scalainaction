package chap04

import scala.collection.parallel.immutable._

object ParallelCollectionsExample {

  def parallelFilter = {
    Vector.range(1, 100000).par.filter(_ % 2 == 0).seq
  } 
  
  def conversion = {
    timedOperation {
      Vector.range(1, 50000).par
    }
    timedOperation {
      val xs = List.iterate(1, 50000)(x => x + 1)
      xs.par
    }
  }
  
  def timedOperation[T](f: => T) = {
    val startTime = System.currentTimeMillis
    f
    println("Time taken = " + (System.currentTimeMillis - startTime))
  } 
}