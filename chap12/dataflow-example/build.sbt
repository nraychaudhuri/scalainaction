name := "DataflowExample"

organization := "scalainaction"

version := "0.2"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-unchecked", "-deprecation")

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions += "-P:continuations:enable"

resolvers += ("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= Seq(
	"com.typesafe.akka" % "akka-actor" % "2.0.3",
  "org.specs2" %% "specs2" % "1.9" % "test"
)

