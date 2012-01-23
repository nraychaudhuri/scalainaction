// Start the scala REPL and run via> :load HigherKind.scala

//List("1", "2", "3").coerce[Set[Int]]

trait TypeConverter[-A, +B] {
  def apply(a: A): B
}

trait HigherKindConveter[-AA[_], +BB[_]] {
  def apply[A, B](a: AA[A])(fo: TypeConverter[A, B]): BB[B]
}


implicit object StringToIntConverter extends TypeConverter[String, Int] {
  def apply(str: String): Int = str.toInt
}

implicit object ListToSetConverter extends HigherKindConveter[List, Set] {
  def apply[A, B](xs: List[A])(converter: TypeConverter[A, B]) = Set(xs map {converter(_)}:_*)
}


def convert[A, B, AA[_], BB[_]](source: AA[A])(implicit hkConverter: HigherKindConveter[AA, BB], fo: TypeConverter[A, B]) = {
  hkConverter(source)(fo)
}

println(convert[String, Int, List, Set](List("1", "2", "3")))
