name := "ActorTest"

version := "1.0"

organization := "Scala in Action"

scalaVersion := "2.10.0" 

libraryDependencies ++= Seq(
	   "org.specs2" %% "specs2" % "1.13" % "test",
	   "org.scala-lang" % "scala-actors" % "2.10.0"
)

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature") 
