name := "ActorTest"

version := "1.0"

organization := "Pillar technology Inc."

scalaVersion := "2.9.1" 

resolvers ++= Seq(
	"Scala Tools Releases" at  "http://scala-tools.org/repo-releases/"
	, "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"
)

libraryDependencies ++= Seq(
	   "org.specs2" %% "specs2" % "1.7" % "test"
)
