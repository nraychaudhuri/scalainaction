name := "WordCount"

version := "1.0"

organization := "Scala in Action"

scalaVersion := "2.10.0" 

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0"
)
