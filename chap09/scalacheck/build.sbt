name := "Test"

version := "1.0"

organization := "XXX"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq (
  "org.scala-tools.testing" % "scalacheck_2.9.1" % "1.9" % "test"
)

// val scalaToolsReleases = "Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"