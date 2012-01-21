name := "Dependency Inject Patterns"

version := "1.0"

organization := "Scala in action"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
	  "org.specs2" %% "specs2" % "1.7.1" % "test"
	, "org.springframework" % "spring" % "2.5.6" % "test"
	, "org.springframework" % "spring-test" % "2.5.6"
	, "junit" % "junit" % "4.10" % "test"
	, "com.novocode" % "junit-interface" % "0.8" % "test"
	, "com.jayway.awaitility" % "awaitility-scala" % "1.3.3"
)
