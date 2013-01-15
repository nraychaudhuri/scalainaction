// Run with >scala UAPExample.scala  or
// run with the REPL in chap01/ via
// scala> :load UAPExample.scala

class UAPExample {
  val someField = "hi"
  def someMethod = "there"
}

object UAPExample extends App {
  val o = new UAPExample
  println(o.someField)
  println(o.someMethod)  
}
