name := "chapter9 examples"

scalaVersion := "2.10.0"

organization := "Scala in Action"

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked", "-language:_")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"
