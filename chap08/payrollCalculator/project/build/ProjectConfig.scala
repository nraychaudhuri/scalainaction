import sbt._

class ProjectConfig(info: ProjectInfo) extends DefaultProject(info)
{
   val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-releases/"
  //Test dependencies
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.6" % "test" 
}