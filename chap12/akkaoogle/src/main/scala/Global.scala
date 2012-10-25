import com.akkaoogle.infrastructure._
import org.h2.tools.Server
import com.akkaoogle.db.AkkaoogleSchema._


object Global extends com.typesafe.play.mini.Setup(com.akkaoogle.http.App) {
	println("initializing the Akkaoogle schema")
  createSchema()
  AkkaoogleActorServer.run()	
} 