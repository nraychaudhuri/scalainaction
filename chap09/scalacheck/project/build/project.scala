import sbt._

class ScalaCheckProject(info: ProjectInfo) extends DefaultProject(info) {
  val scalaToolsReleases = "Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"
  val scalaCheck = "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.8"
}