// Quickreference: https://github.com/harrah/xsbt/wiki/Quick-Configuration-Examples

name := "SimpleProject"

version := "1.0"

organization := "Scala in Action"

scalaVersion := "2.9.1"

resolvers += "Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"

libraryDependencies ++= Seq (
  "org.specs2" %% "specs2" % "1.7" % "test"
  , "org.eclipse.jetty" % "jetty-server" % "7.0.0.RC2"
)

mainClass in (Compile, run):= Some("HelloWorld")

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked")