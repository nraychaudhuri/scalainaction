import scala.actors._, Actor._
import scala.actors.remote._

val heartBeatActor = actor {
  RemoteActor.alive(9999)
  RemoteActor.register('HB, self)
  while(true) {
    receive {
      case 'HeartBeat => reply("I am alive")
    }
  }
}

val heartBeatActorHandle = RemoteActor.select(Node("127.0.0.1", 9999), 'HB)

heartBeatActorHandle !? (50, 'HeartBeat) match {
  case Some(_) => println("server is up")
  case None => println("it timedout, maybe server is down")
}






  