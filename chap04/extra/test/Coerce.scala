// Start scala and run via> :load Coerce.scala

trait FOCoercible[-A, +B] {
  def apply(a: A): B
}

trait FOBicoercible[A, B] extends FOCoercible[A, B] {
  def unapply(b: B): A
}

// object FOCoercible {
  implicit object StringToIntCoercion extends FOCoercible[String, Int] {
    def apply(str: String) = str.toInt
  }
// }

implicit def foidentityCoercion[A]: FOBicoercible[A, A] = new FOBicoercible[A, A] {
  def apply(a: A) = a
  def unapply(a: A) = a
}


trait HOCoercible[-AA[_], +BB[_]] {
  def apply[A, B](a: AA[A])(implicit fo: FOCoercible[A, B]): BB[B]
}

trait HOBicoercible[AA[_], BB[_]] extends HOCoercible[AA, BB] {
  def unapply[A, B](a: BB[B])(implicit fo: FOBicoercible[A, B]): AA[A]
}

// object HOCoercible {
  implicit object ListToSetCoercion extends HOCoercible[List, Set] {
    def apply[A, B](xs: List[A])(implicit fo: FOCoercible[A, B]) = Set(xs map { fo(_) }: _*)
  }
// }


trait HOCoercible2To1[-AA[_, _], +BB[_]] {
  def apply[A, B, C](a: AA[A, B])(implicit fo: FOCoercible[(A, B), C]): BB[C]
}

implicit object MapToListCoercion extends HOCoercible2To1[Map, List] {
  def apply[A, B, C](m: Map[A, B])(implicit fo: FOCoercible[(A, B), C]) = m map { fo(_) } toList
}



implicit def applyHOCoercible[A, B, AA[_], BB[_]](implicit ho: HOCoercible[AA, BB], fo: FOCoercible[A, B]): FOCoercible[AA[A], BB[B]] = new FOCoercible[AA[A], BB[B]] {
  def apply(a: AA[A]) = ho(a)
}

implicit def applyHOCoercible2To1[A, B, C, AA[_, _], BB[_]](implicit ho: HOCoercible2To1[AA, BB], fo: FOCoercible[(A, B), C]): FOCoercible[AA[A, B], BB[C]] = new FOCoercible[AA[A, B], BB[C]] {
  def apply(a: AA[A, B]) = ho(a)
}

// implicit def applyHOBicoercible[A, B, AA[_], BB[_]](implicit ho: HOBicoercible[AA, BB], fo: FOBicoercible[A, B]): FOCoercible[AA[A], BB[B]]


implicit def coercibleSyntax[A](a: A) = new {
  def coerce[B](implicit fo: FOCoercible[A, B]) = fo(a)
}

implicit def bicoercibleSyntax[A](a: A) = new {
  def uncoerce[B](implicit fo: FOBicoercible[B, A]) = fo unapply a    // maybe unapply isn't the best name
}
