package chap11.top.artists.db

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.{Session, SessionFactory}
import java.util.{List => JList }
import chap11.top.artists.model.Artist

trait ArtistDb {
  def findAll: JList[Artist]
  def save(artist: Artist): Long
}

@Repository
class ArtistRepository extends ArtistDb {
  @Autowired
  var sessionFactory: SessionFactory = null
    
  @Transactional
  def save(artist: Artist): Long = currentSession.save(artist).asInstanceOf[Long]
        
  @Transactional(readOnly = true)
  def findAll: JList[Artist] = currentSession.createCriteria(classOf[Artist]).
    list().asInstanceOf[JList[Artist]]
  
  private def currentSession = sessionFactory.getCurrentSession
}