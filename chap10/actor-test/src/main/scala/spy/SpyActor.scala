package spy

import scala.actors.{Actor, TIMEOUT}
import Actor._
import scala.language.implicitConversions

class SpyActor(currentActor: Actor, timeout: Long) {
  
  def last(count: Int) = {
    (1 to count) map {i => ?(timeout) }    
  }
  
  def lastOne = {
    ?(timeout)
  }
  
  def none = {
    ?(timeout) == TIMEOUT
  }
  
  private[this] def ?(msecs: Long) = receiveWithin(msecs) {
    case m => m
  }
}


object SpyActor {
  implicit def intToStringWrapper(count: Int) = new {
    def messages = count
  }
  
  def message = 1

  def apply(timeout: Long) = {
    new SpyActor(Actor.self, timeout)
  }
}