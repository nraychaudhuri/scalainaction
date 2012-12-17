package chap10.fp.examples

object Combinators {
  implicit def kestrel[A](a: A) = new {
    def tap(sideEffect: A => Unit): A = {
      sideEffect(a)
      a
    }
  }
}

case class Person(firstName: String, lastName: String)
case class Mailer(mailAddress: String) {
  def mail(body: String) = {
    println("send mail here...")
  }
}

object Main {
  import Combinators._
  def main(args: Array[String]): Unit = {
    Person("Nilanjan", "Raychaudhuri").tap(p => {
     println("First name " + p.firstName) 
     Mailer("some address").mail("new person joined " + p)
    }).lastName
  }
}