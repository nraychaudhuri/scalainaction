import sbt._
import Keys._
import com.github.siasia.WebPlugin._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := "scalainaction",
    version := "0.2",
    scalaVersion := "2.9.1",
    scalacOptions := Seq("-unchecked", "-deprecation")
  )
}

object Resolvers {
  val scalaToolsReleases = "Scala-Tools Maven2 Releases Repository" at "http://scala-tools.org/repo-releases"
  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
}

object ScalazDependencies {
  lazy val scalazVersion = "6.0.3"
  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % scalazVersion
  lazy val scalazHttp = "org.scalaz" %% "scalaz-http" % scalazVersion
}

object JettyDependencies {
  lazy val jettyVersion = "7.3.0.v20110203"
  lazy val jettyServlet = "org.eclipse.jetty" % "jetty-servlet" % jettyVersion % "container"
  lazy val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "test, container"
  lazy val jettyServer = "org.eclipse.jetty" % "jetty-server" % jettyVersion % "container"
}

object H2DatabaseDependencies {
  val h2 = "com.h2database" % "h2" % "1.2.137"
  val squeryl = "org.squeryl" % "squeryl_2.9.0-1" % "0.9.4"
}

object H2TaskManager {
  var process: Option[Process] = None
  lazy val H2 = config("h2") extend(Compile)
  
  val startH2 = TaskKey[Unit]("start", "Starts H2 database")
  val startH2Task = startH2 in H2 <<= (fullClasspath in Compile) map { cp =>
      startDatabase(cp.map(_.data).map(_.getAbsolutePath()).filter(_.contains("h2database")))}         
      
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
}

object MainBuild extends Build {
 import Resolvers._
 import BuildSettings._
 import JettyDependencies._
 import ScalazDependencies._
 import H2DatabaseDependencies._
 import H2TaskManager._

 val h2Tasks = Seq(stopH2Task, startH2Task)
 lazy val wekanban = Project(
   "wekanban",
   file("."),
   settings = buildSettings ++ webSettings ++ Seq (
   libraryDependencies ++= Seq(jettyServlet, jetty7, jettyServer, scalazCore, scalazHttp, h2, squeryl)) ++ h2Tasks) 



}