import scala.language.dynamics

class MyMap extends Dynamic {
  def selectDynamic(fieldName: String) = map.get(fieldName)
  private val map = Map("foo" -> "1", "bar" -> 2)
}

object DynamicMapExample extends App {
  val someMap = new MyMap 
  println(someMap.foo)
  println(someMap.bar)  
}
