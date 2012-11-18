package countwords

import akka.actor._
import com.typesafe.config.ConfigFactory

object WorkerSystem extends App {
	val workerSystem = ActorSystem("workersystem", ConfigFactory.load.getConfig("workersystem"))  
	println("Started the workersystem")
}