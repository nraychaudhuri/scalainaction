// Start scala >scala
// load into the REPL> :load FunctionalObjects.scala
package chap04

object FunctionalObject {
	
	object foldl {
	  def apply[A, B](xs: Traversable[A], defaultValue: B)(op: (B, A) => B) =  (defaultValue /: xs)(op)
	}


	object ++ extends Function1[Int, Int]{
	  def apply(p: Int): Int = p + 1
	}

	// val ++ = (x: Int) => x + 1
	// 
	// object ++ extends (Int => Int) {
	//   def apply(p: Int): Int = p + 1
	// }

	object sqrt extends Function1[Double, Double] {

	  def apply(p: Double): Double = if(p == 0) 0 else sqrt_iterator(1, p)

	  def sqrt_iterator(guess: Double, p: Double): Double = {
	    if(good_enough(guess, p)) guess
	    else sqrt_iterator(improve(guess, p), p)
	  }
	  def improve(guess: Double, p: Double) = {
	    def average(x: Double, y: Double) = (x + y) / 2
	    average(guess, p / guess)  
	  }
	  def good_enough(guess: Double, p: Double) = 
	      Math.abs((guess * guess) - p) < 0.001
	}	
	
	val addOne: Int => Int = x => x + 1
	val addTwo = addOne compose addOne
}

