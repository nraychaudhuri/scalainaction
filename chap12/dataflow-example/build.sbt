name := "DataflowExample"

organization := "scalainaction"

version := "0.3"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions += "-P:continuations:enable"

resolvers += ("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-dataflow" % "2.1.0",
  "org.specs2" %% "specs2" % "1.13" % "test"
)

