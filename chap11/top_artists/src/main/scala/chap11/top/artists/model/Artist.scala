package chap11.top.artists.model
import reflect.BeanProperty
import javax.persistence._
import scala.annotation.target.field
@Entity
class Artist {
  @(Id @field) @(GeneratedValue @field) @BeanProperty
  var id: Long = 0
  @BeanProperty
  var name: String = ""
  @BeanProperty
  var playCount: Long = 0
  @BeanProperty
  var listeners: Long = 0
}
object Artist {
  def apply(name: String, playCount: Long, listeners: Long) = {
    val a = new Artist
    a.name = name
    a.playCount = playCount
    a.listeners = listeners
    a
} }