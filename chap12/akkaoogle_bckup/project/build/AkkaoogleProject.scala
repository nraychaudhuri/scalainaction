import sbt._

class MyProject(info: ProjectInfo)
  extends DefaultWebProject(info)
  with AkkaProject
  with IdeaProject
  with H2Actions {
 
    val akkaStm = akkaModule("stm")
    val akkaTypedActor = akkaModule("typed-actor")
    val akkaRemote = akkaModule("remote")
    val akkaHttp = akkaModule("http")
    val akkaAmqp = akkaModule("amqp")
    val akkaCamel = akkaModule("camel")
    val akkaSpring = akkaModule("spring")
    val akkaJta = akkaModule("jta")
    val akkaKernel = akkaModule("kernel")

    val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"
    val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8-SNAPSHOT" % "test"

    val h2 = "com.h2database" % "h2" % "1.2.137"
    val squeryl = "org.squeryl" % "squeryl_2.8.1" % "0.9.4-RC6"
    val jettyAll = "org.eclipse.jetty" % "jetty-distribution" % "8.0.0.M2"
    val jettyAllTest = "org.eclipse.jetty" % "jetty-distribution" % "8.0.0.M2" % "test"
}


trait H2Actions { this: DefaultWebProject =>
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