// Start scala >scala
// load into the REPL> :load HigherOrderFunctions.scala
package chap04

object HigherOrderFunctionsExample {
	def addOne(num: Int) = {          
	  def ++ = (x:Int) => x + 1
	  ++(num)
	}

	def map[A, B](xs: List[A], f: A => B): List[B] = {
	  xs match {
	    case List() => List.empty[B]
	    case head :: tail => f(head) :: map(tail, f)
	  }
	}

	def map1[A, B](xs: List[A], f: A => B): List[B] = for(x <- xs) yield f(x)

	def map2[A, B](xs: List[A])(f: A => B): List[B] = {
	  val startValue = List.empty[B]
	  xs.foldRight(startValue) { f(_) :: _ }
	}

	def map3[A, B](xs: List[A])(f: A => B): List[B] = {
	  val startValue = List.empty[B]
	  xs.foldLeft(startValue)((a, x) => f(x) :: a).reverse
	}

  def exists[A](xs: List[A], e: A) = xs.foldLeft(false)((a, x) => a || (x == e))

	def flatten[B](xss: List[List[B]]): List[B] = {
	  xss match {
	    case List() => List.empty[B]
	    case head :: tail => head ::: flatten(tail)
	  }
	}

	def flatten3[B](xss: List[List[B]]): List[B] = {
		def _flatten3(oldList: List[List[B]], newList: List[B]): List[B] = oldList match {
		  case List() => newList
		  case head :: tail => _flatten3(tail, newList ::: head)
		}
		_flatten3(xss, Nil)
	}

	def flatMap[A, B](xs: List[A])(f: A => List[B]) : List[B] = {
	  flatten(map(xs, f))
	}

	def flatten2[B](xss: List[List[B]]): List[B] = {
	  val startValue = List.empty[B]
	  xss.foldRight(startValue) { _ ::: _ }
	}

	def flatMap2[A, B](xs: List[A])(f: A => List[B]) : List[B] = {
	  flatten2(map2(xs)(f))
	}

	val r = flatMap2(List("one", "two", "three")) {_.toList}
	println(r)	
}



