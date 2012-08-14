name := "StmExample"

organization := "scalainaction"

version := "0.2"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers ++= Seq(
	"Akka Repo" at "http://akka.io/repository",
	"Typesafe Repo" at "http://repo.typesafe.com/typesafe/repo"
	)

libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka-stm" % "1.1-M1",
  "org.specs2" %% "specs2" % "1.9" % "test"
)

