package com.kanban.models

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations._
import KanbanSchema._

class Story(val number: String, val title: String, val phase: String){
  
  private def phaseLimits = Map("ready" -> Some(3), "dev" -> Some(2), "test" -> Some(2), "deploy" -> None)

  private[this] def validate = {
    if(number.isEmpty || title.isEmpty) { 
      throw new ValidationException("Both number and title are required")
    }
    if(!stories.where(a => a.number === number).isEmpty) { 
      throw new ValidationException("The story number is not unique") 
    }
  }
  
  private[this] def validateLimit(phase: String) = {
    val currentSize:Long = from(stories)(s => where(s.phase === phase) compute(count))
    if(currentSize == phaseLimits(phase).getOrElse(-1)) { 
      throw new ValidationException("You cannot exceed the limit set for the phase.") 
    }
  }
  
  
  def moveTo(phase: String): Either[Throwable, String] = {
    tx {
      try {
        validateLimit(phase)
        update(stories)(s =>
      	  where(s.number === this.number)
      	  set(s.phase := phase)
        )
        Right("Card " + this.number + " is moved to " + phase + " phase successfully.")
      } catch {
        case exception: Throwable => exception.printStackTrace ; Left(exception)        
      }
    }
  }
  
  def save(): Either[Throwable, String] = {
    tx {
      try {
        validate
        stories.insert(this)          
        Right("Story is created successfully")
      } catch {
        case exception: Throwable => Left(exception)
      }
    }
  }
}

object Story {
  def apply(number: String, title: String) = new Story(number, title, "ready")
  
  def findAllByPhase(phase: String): Iterable[Story] = tx { from(stories)(s => where(s.phase === phase) select(s)) map(s => s) }
  
  def findByNumber(number: String): Story = tx { stories.where(s => s.number === number).single }
}

class ValidationException(message: String) extends RuntimeException(message)