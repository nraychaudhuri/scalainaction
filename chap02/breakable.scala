// Run with >scala breakable.scala  or
// load into the REPL in chap02/ via
// scala> :load breakable.scala

val breakException = new RuntimeException("break exception")
def breakable(op: => Unit) {
  try {
    op
  } catch { case _ => }
}

def break = throw breakException

def install = {
  val env = System.getenv("SCALA_HOME")
  if(env == null) break
  println("found scala home lets do the real work")
}


//passing the method name
//breakable(install) 

//as a closure
breakable {
  val env = System.getenv("SCALA_HOME")
  if(env == null) break
  println("found scala home lets do the real work")
}
