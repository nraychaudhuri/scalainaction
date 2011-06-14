import sbt._

class DiProject(info: ProjectInfo) extends DefaultProject(info) {
  val spring = "org.springframework" % "spring" % "2.5.6"
  val springTest = "org.springframework" % "spring-test" % "2.5.6"
  val junit = "junit" % "junit" % "4.4" % "test"

  val junitInterface = "com.novocode" % "junit-interface" % "0.5" % "test"

  val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8-SNAPSHOT" % "test"
  
  val awaitility = "com.jayway.awaitility" % "awaitility-scala" % "1.3.1"
}