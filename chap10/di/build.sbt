name := "Dependency Inject Patterns"

version := "1.0"

organization := "Scala in action"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
	  "org.specs2" %% "specs2" % "1.13" % "test"
	, "org.springframework" % "spring" % "2.5.6" % "test"
	, "org.springframework" % "spring-test" % "2.5.6"
	, "junit" % "junit" % "4.10" % "test"
	, "com.novocode" % "junit-interface" % "0.8" % "test"
	, "com.jayway.awaitility" % "awaitility-scala" % "1.3.3",
	"org.scala-lang" % "scala-actors" % "2.10.0"
)

// append options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature") 
