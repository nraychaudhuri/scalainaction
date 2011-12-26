name := "Dependency Inject Patterns"

version := "1.0"

organization := "Scala in action"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
	  "org.scala-tools.testing" % "specs_2.9.1" % "1.6.9" % "test"
	, "org.springframework" % "spring" % "2.5.6" % "test"
	, "org.springframework" % "spring-test" % "2.5.6"
	, "junit" % "junit" % "4.10" % "test"
	, "com.novocode" % "junit-interface" % "0.7" % "test"
	, "com.jayway.awaitility" % "awaitility-scala" % "1.3.3"
)


