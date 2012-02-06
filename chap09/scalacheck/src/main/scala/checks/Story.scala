package checks

import java.util.Date

class Story(val id: Long, val number: String, val title: String, val dateStarted: Date) {
  
  def ==(anotherStory: Story) = {
    if(null == anotherStory) false
    else
      id == anotherStory.id && 
        number == anotherStory.number && 
        title == anotherStory.title && 
        dateStarted == anotherStory.dateStarted
  }
  
  override def toString = {
    title + id + number + dateStarted
  }
}