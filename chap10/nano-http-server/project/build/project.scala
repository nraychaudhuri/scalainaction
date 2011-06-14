import sbt._

class MyProject(info: ProjectInfo) extends DefaultProject(info) { 
  val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"
  val specs = "org.specs2" % "specs2_2.9.0" % "1.4-SNAPSHOT" % "test"
  
  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
}
