name := "StmExample"

organization := "scalainaction"

version := "0.2"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += ("releases" at
    "http://oss.sonatype.org/content/repositories/releases")

libraryDependencies ++= Seq(
	"org.scala-tools" %% "scala-stm" % "0.6",
  "org.specs2" %% "specs2" % "1.9" % "test"
)

