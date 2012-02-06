// Start scala >scala
// load into the REPL> :load HigherOrderFunctions.scala

def addOne(num: Int) = {          
  def ++ = (x:Int) => x + 1
  ++(num)
}

def map[A, B](xs: List[A], f: A => B): List[B] = {
  xs match {
    case List() => List[B]()
    case head :: tail => f(head) :: map(tail, f)
  }
}

def map1[A, B](xs: List[A], f: A => B): List[B] = for(x <- xs) yield f(x)

def map2[A, B](xs: List[A])(f: A => B): List[B] = {
  val startValue = List[B]()
  xs.foldRight(startValue) { f(_) :: _ }
}


def flatten[B](xss: List[List[B]]): List[B] = {
  xss match {
    case List() => List[B]()
    case head :: tail => head ::: flatten(tail)
  }
}

def flatMap[A, B](xs: List[A])(f: A => List[B]) : List[B] = {
  flatten(map(xs, f))
}

def flatten2[B](xss: List[List[B]]): List[B] = {
  val startValue = List[B]()
  xss.foldRight(startValue) { _ ::: _ }
}

def flatMap2[A, B](xs: List[A])(f: A => List[B]) : List[B] = {
  flatten2(map2(xs)(f))
}

val r = flatMap2(List("one", "two", "three")) {_.toList}
println(r)


