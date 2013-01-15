// Start scala >scala
// load into the REPL> :load ForComprehension.scala
package chap04
object ForComprehension extends App {
	case class Artist(name: String, genre: String)

	val artists = List(Artist("Pink Floyd", "Rock"),
	                   Artist("Led Zeppelin", "Rock"),
	                   Artist("Michael Jackson", "Pop"),
	                   Artist("Above & Beyond", "trance")  
	                  )

	//for comprehension with yield                  
	for(Artist(name, genre) <- artists; if(genre == "Rock")) yield name

	artists withFilter { 
	  case Artist(name, genre) => genre == "Rock"
	} map { 
	  case Artist(name, genre) => name
	}

	//for comprehension without yield
	for(Artist(name, genre) <- artists) println(name + "," + genre)

	artists withFilter {
	  case Artist(name, genre) => true; case _ => false
	} foreach {
	  case Artist(name, genre) => println(name + "," + genre)
	}

	case class ArtistWithAlbums(artist: Artist, albums: List[String])
	val artistsWithAlbums = List(
	                   ArtistWithAlbums(Artist("Pink Floyd", "Rock"), List("Dark side of the moon", "Wall")),
	                   ArtistWithAlbums(Artist("Led Zeppelin", "Rock"), List("Led Zeppelin IV", "Presence")),
	                   ArtistWithAlbums(Artist("Michael Jackson", "Pop"),List("Bad", "Thriller")),
	                   ArtistWithAlbums(Artist("Above & Beyond", "trance"), List("Tri-State", "Sirens of the Sea"))  
	                  )

	for { ArtistWithAlbums(artist, albums) <- artistsWithAlbums  
	      album <- albums
	      if(artist.genre == "Rock")
	    } yield album


	val rockAlbums = artistsWithAlbums flatMap {
	  case ArtistWithAlbums(artist, albums) => albums withFilter { 
	                                              album => artist.genre == "Rock"
	                                          } map { case album => album }
	}
	println(rockAlbums)
	
}





