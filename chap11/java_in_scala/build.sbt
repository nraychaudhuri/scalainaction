organization := "Scala in Action"

name := "java_in_scala"

version := "0.1"

libraryDependencies += "joda-time" % "joda-time" % "2.0"

scalaVersion := "2.10.0"

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature") 
