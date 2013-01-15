// Run with >scala HigherOrderFunctions.scala  or
// run with the REPL in chap01/ via
// scala> :load HigherOrderFunctions.scala

object HigherOrderFunctionsExample extends App {
  //function as value
  val inc = (x : Int) => x + 1
  println(inc(1))

  //function as parameter
  println(List(1, 2, 3).map((x: Int) => x + 1))

  //function as object
  println( List(1, 2, 3).map(new Function1[Int, Int]{ def apply(x:Int): Int = x + 1}) )  
}
