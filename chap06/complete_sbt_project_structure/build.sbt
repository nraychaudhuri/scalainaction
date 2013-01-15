scalaVersion := "2.10.0"

name := "Testing SBT"

version := "1.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-server" % "7.0.0.RC2",
  "org.scala-tools.testing" % "specs" % "1.6.2" % "test"
)