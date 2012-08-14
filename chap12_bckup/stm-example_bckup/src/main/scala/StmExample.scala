package stmexample

import akka.stm._
import collection.immutable.HashMap


object StmExample {
  val ref1 = Ref(HashMap[String, Any](
     "service1" -> "10",
     "service2" -> "20",
     "service3" -> null))
  val ref2 = Ref(HashMap[String, Int]())

  def swap(key: String) = {
    try {
      atomicSwap(key)
    } catch {
      case _ => println("exception occurred")
    }
  }

  def atomicSwap(key: String) = atomic {
    val value: Option[Any] = atomicDelete(key)
    atomicInsert(key, Integer.parseInt(value.get.toString))
  }

  def atomicDelete(key: String): Option[Any] = atomic {
    val oldMap = ref1.get
    val value = oldMap.get(key)
    ref1.alter(_ - key)
    value
  }

  def atomicInsert(key: String, value: Int) = atomic {
    val oldMap = ref2.get
    val newMap = oldMap + ( key -> value)
    ref2.swap(newMap)
  }
}