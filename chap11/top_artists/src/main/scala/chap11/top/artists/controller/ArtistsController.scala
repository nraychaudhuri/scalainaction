package chap11.top.artists.controller

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod._
import chap11.top.artists.db.ArtistDb
import chap11.top.artists.model.Artist
import org.springframework.web.servlet.ModelAndView
import dispatch._
import scala.xml.Node

@Controller
class ArtistsController {
  @Autowired
  val db: ArtistDb = null
  
  @RequestMapping(value = Array("/artists"), method = Array(GET))
  def loadArtists() =
    new ModelAndView("artists", "topArtists", db.findAll)  
  
  @RequestMapping(value = Array("/refresh"), method = Array(GET))  
  def refresh() = {
    retrieveAndLoadArtists()
    new ModelAndView("artists", "topArtists", db.findAll) 
  }
  
  private def retrieveAndLoadArtists() {
    val rootUrl = "http://ws.audioscrobbler.com/2.0/" 
    val apiMethod = "chart.gettopartists"
    val apiKey = sys.props("api.key")
    val req = url(rootUrl + "?method=" + apiMethod + "&api_key=" + apiKey)
    Http(req OK as.xml.Elem).map {resp =>
        val artists = resp \\ "artist"
        artists.foreach {node =>
          val artist = makeArtist(node)
          println(artist.name)
          db.save(artist)
        }
      }()  //applying the promise
  }  
  
  private def makeArtist(n: Node) = {
    val name = (n \ "name").text
    val playCount = (n \ "playcount").text.toLong
    val listeners = (n \ "listeners").text.toLong
    Artist(name = name, playCount = playCount, listeners = listeners)
  }
}