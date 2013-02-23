import sbt._
import Keys._

object AkkaoogleBuild extends Build with ConfigureScalaBuild {

 	import H2TaskManager._

  lazy val root = scalaMiniProject("com.akkaoogle","akkaoogle","1.0")
   .settings(startH2Task, stopH2Task)
   .settings(
	  organization := "scalainaction",
	  scalaVersion := "2.10.0",
	  scalacOptions ++= Seq("-unchecked", "-deprecation"),

    resolvers += ("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"),
    parallelExecution in Test := false
	)
   .settings(
   	 libraryDependencies ++= Seq(
	     "com.typesafe.akka" %% "akka-actor" % "2.1.0",
	     "com.typesafe.akka" %% "akka-remote" % "2.1.0",
	     "com.typesafe.akka" %% "akka-agent" % "2.1.0",
	     "com.h2database" % "h2" % "1.2.127",
       "org.squeryl" % "squeryl_2.10.0-RC5" % "0.9.5-5",
       "org.specs2" %% "specs2" % "1.13" % "test",
       "org.eclipse.jetty" % "jetty-distribution" % "8.0.0.M2" % "test"
	 )) 
}

trait ConfigureScalaBuild {

  
  lazy val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  
  lazy val typesafeSnapshot = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  
  val netty = Some("play.core.server.NettyServer") 

  def scalaMiniProject(org: String, name: String, buildVersion: String, baseFile: java.io.File = file(".")) = Project(id = name, base = baseFile, settings = Project.defaultSettings).settings(
    version := buildVersion,
    organization := org,
    resolvers += typesafe,
    resolvers += typesafeSnapshot,
    libraryDependencies += "com.typesafe" %% "play-mini" % "2.1-RC2",
    mainClass in (Compile, run) := netty,
    ivyXML := <dependencies> <exclude org="org.springframework"/> </dependencies>
  )
}

object H2TaskManager {
  var process: Option[Process] = None
  lazy val H2 = config("h2") extend(Compile)
  
  val startH2 = TaskKey[Unit]("start", "Starts H2 database")
  val startH2Task = startH2 in H2 <<= (fullClasspath in Compile) map { cp =>
      startDatabase(cp.map(_.data).map(_.getAbsolutePath()).filter(_.contains("h2database")))}         
     
      def startDatabase(paths: Seq[String]) = {
        process match {
          case None =>
            val cp = paths.mkString(System.getProperty("path.seperator"))
            val command = "java -cp " + cp + " org.h2.tools.Server"
            println("Starting Database with command: " + command)
            process = Some(Process(command).run())
            println("Database started ! ")
          case Some(_) =>
            println("H2 Database already started")
        }
      }     
      
  val stopH2 = TaskKey[Unit]("stop", "Stops H2 database")
  val stopH2Task = stopH2 in H2 :={
    process match {
      case None => println("Database already stopped")
      case Some(_) =>
        println("Stopping database...")
        process.foreach{_.destroy()}
        process = None
        println("Database stopped...")
    }
  }  
}
