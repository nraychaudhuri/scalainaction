def position[A](xs: List[A], value: A): Maybe[Int] = {
  val index = xs.indexOf(value)
  if(index != -1) Just(index) else Nil
}

def defaultToNull[A <: Maybe[_]](p: A) = {
  p.getOrElse(null)
}
