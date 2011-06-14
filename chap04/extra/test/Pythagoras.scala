// def pythagoras[T](a: T, b: T, sqrt: T => T)(implicit n: Numeric[T]) = {
//   import n.mkNumericOps
//   sqrt(a*a + b*b)
// }
import Numeric._
def pythagoras[Numeric[T], T](a: T, b: T, sqrt: T => T) = {
  sqrt(a*a + b*b)
}

def intSqrt(n: Int) = Math.sqrt(n).toInt

val x = pythagoras(3,4, intSqrt)
println(x)
