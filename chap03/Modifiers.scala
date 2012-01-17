// Can be compiled with >scalac Modifiers.scala 
// and made available in the REPL via> scala -cp .

package outerpkg.innerpkg {

class Outer {
  class Inner { 
    private[Outer] def f()  = "This is f"
    private[innerpkg] def g() = "This is g" 
    private[outerpkg] def h() = "This is h"
  }
}

}

trait DogMood {
  def greet 
}

trait AngryMood extends DogMood {
  abstract override def greet = {
    println("bark")
    super.greet
  }
}
