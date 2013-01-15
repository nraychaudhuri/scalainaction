// You can run this in the REPL in chap10/ with  
// scala> :load PurelyFunctionalProgram.scala
// or the paste mode see scala> :help

object PureFunctionalProgram {
  def main(args: Array[String]): Unit = singleExpression(args.toList)
  
  def singleExpression: List[String] => (List[Int], List[Int]) = { a =>
    a map (_.toInt) partition (_ < 30)
  }
}
