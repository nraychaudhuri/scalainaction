name := "wordCountRemoteAkka"

organization := "scalainaction"

version := "0.3"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

javacOptions += "-Xlint:deprecation"

resolvers ++= Seq(
	"Typesafe Repo" at "http://repo.typesafe.com/typesafe/repo"
)

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.1.0",
	"com.typesafe.akka" %% "akka-remote" % "2.1.0",
  "org.specs2" %% "specs2" % "1.13" % "test"
)

