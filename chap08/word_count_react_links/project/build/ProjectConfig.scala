import sbt._

class ProjectConfig(info: ProjectInfo) extends DefaultProject(info)
{
   val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"
  //Test dependencies
  val specs = "org.scala-tools.testing" % "specs" % "1.6.2" % "test" 
}