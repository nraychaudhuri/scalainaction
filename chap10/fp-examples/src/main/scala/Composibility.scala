package chap10.fp.examples

object Composibility {
  def even: Int => Boolean = _ % 2 == 0  
  def not: Boolean => Boolean = !_
  def filter[A](criteria: A => Boolean)(col: Traversable[A])  = col.filter(criteria)
  def map[A, B](f: A => B)(col: Traversable[A]) = col.map(f)

  def evenFilter = filter(even) _
  def double: Int => Int = _ * 2
  def doubleAllEven = evenFilter andThen map(double)
  
  def odd: Int => Boolean = not compose even
  def oddFilter = filter(odd) _
  def doubleAllOdd = oddFilter andThen map(double)
  //filter(Vector(1, 2, 5, 6, 7, 14))(even).map(_ * 2)
  doubleAllEven(Vector(1, 2, 5, 6, 7, 14))
}