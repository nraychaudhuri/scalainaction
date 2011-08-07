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
    val mailer = Mailer("some address")
    Person("Nilanjan", "Raychaudhuri").tap(p => {
     println("First name " + p.firstName) 
     mailer.mail("new person joined " + p)
    }).lastName
  }
}