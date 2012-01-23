name := "ScalaCheckExample"

version := "1.0"

organization := "Scala in Action"

scalaVersion := "2.9.1"

resolvers += "Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"

libraryDependencies ++= Seq (
  "org.scala-tools.testing" % "scalacheck_2.9.1" % "1.9" % "test"
)


// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"