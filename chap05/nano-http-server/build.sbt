name := "Nano Http server"

version := "1.0"

organization := "Scala in Action"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
	"org.specs2" % "specs2_2.9.1" % "1.7" % "test"
)

  //val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"

  
  //def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  //override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"