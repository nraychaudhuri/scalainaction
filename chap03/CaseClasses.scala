case class Person(firstName:String, lastName: String)
object Person {
  def apply(firstName:String, lastName:String) = {
    new Person(firstName, lastName)
  }
  def unapply(p:Person): Option[(String, String)] = Some((p.firstName, p.lastName)) 
}

val p = Person("Matt", "vanvleet")

p match {
  case Person(first, last) => println(">>>> " + first + ", " + last)
}


