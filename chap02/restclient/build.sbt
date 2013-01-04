name := "RestClient"

scalaVersion := "2.9.2"

organization := "Scala in Action"

libraryDependencies += "commons-logging" % "commons-logging" % "1.1.1"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.2"

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked")
