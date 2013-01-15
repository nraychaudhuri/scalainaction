// Start the REPL and load the file via>:load Linearization.scala

trait A {
  def doIt = println("inside A")
}

class B extends A {
  override def doIt = { println("inside B"); super.doIt }
}

trait C extends A {
  override def doIt = { println("inside C"); super.doIt }  
}

trait D extends A {
  override def doIt = { println("inside D"); super.doIt }
}

class X extends B with C with D

object Linearization extends App {
  val x1 = new X
  x1.doIt  
}
