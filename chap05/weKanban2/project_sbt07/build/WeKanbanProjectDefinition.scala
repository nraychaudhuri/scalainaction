import sbt._

final class WeKanbanProjectDefinition(info: ProjectInfo) extends DefaultWebProject(info) {
  
  val jettyServlet = "org.eclipse.jetty" % "jetty-servlet" % "7.0.0.RC2"
  val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.0.0.RC0" % "test"
  val jettyServer = "org.eclipse.jetty" % "jetty-server" % "7.0.0.RC2"
  
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"
  val scalazCore = "com.googlecode.scalaz" %% "scalaz-core" % "5.0-SNAPSHOT"
  val scalazHttp = "com.googlecode.scalaz" %% "scalaz-http" % "5.0-SNAPSHOT"
  
  val h2 = "com.h2database" % "h2" % "1.2.137"
  val squeryl = "org.squeryl" % "squeryl_2.8.0.Beta1" % "0.9.4beta2"
    
  //adding h2-start and h2-stop actions
  import Process._  
  var h2Process: Process = _  

  val classPathAsString = compileClasspath.get.map(_.absolutePath).mkString(System.getProperty("path.separator"))
  def processBuilder = new java.lang.ProcessBuilder(
      "java", "-cp", classPathAsString , "org.h2.tools.Server") 
  
  lazy val h2Start = task { 
    import scala.concurrent.ops._
    spawn { h2Process = processBuilder.run }
    None 
  } describedAs("Starts a new instance of H2 database server")
  
  lazy val h2Stop = task { 
    h2Process.destroy
    None
  } describedAs("Stops the currently running H2 database server")
  
}
