import sbt._

class ActorTestProject(info: ProjectInfo) extends DefaultProject(info) {
  val scalaToolsReleases = "Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"
  val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"

  lazy val scalaCheck = "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.8"  
  lazy val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8-SNAPSHOT" % "test"
}