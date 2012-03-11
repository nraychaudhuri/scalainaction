// Start scala >scala
// load into the REPL> :load Infinite.scala
package chap04

object InfiniteExample {
	def fib(n: Int): Int = n match {
	  case 0 => 0
	  case 1 => 1
	  case n => fib(n - 1) + fib(n - 2)
	}

	val fib1: Stream[Int]  = Stream.cons(0, Stream.cons(1, fib1.zip(fib1.tail).map(t => t._1 + t._2)))	
}

