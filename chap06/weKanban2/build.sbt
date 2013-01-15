import com.github.siasia.WebPlugin._

name := "weKanban"

organization := "scalainaction"

version := "0.2"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers ++= Seq(
 "Scala-Tools Maven2 Releases Repository" at "http://scala-tools.org/repo-releases",
 "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-http" % scalazVersion,
  "org.eclipse.jetty" % "jetty-servlet" % jettyVersion % "container",
  "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "test, container",
  "org.eclipse.jetty" % "jetty-server" % jettyVersion % "container",
  "com.h2database" % "h2" % "1.2.137",
  "org.squeryl" % "squeryl_2.10" % "0.9.5-6"
)

seq(webSettings :_*)