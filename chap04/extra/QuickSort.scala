// Start the scala REPL and run via> :load QuickSort.scala

def sort(xs: List[Int]): List[Int] = xs match { 
  case Nil => xs
  case _ => {
    val pivot = xs(xs.size / 2) 
    sort(xs filter (pivot >)) ++ (xs filter (pivot == )) ++ sort(xs filter (pivot <))
  }
}

val items  = List(2, 7, 10, 5, 4, 3)

println(sort(items))