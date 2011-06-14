import sbt._

class MyProject(info: ProjectInfo)
  extends DefaultProject(info)
  with AkkaProject
  with IdeaProject { 
    val akkaStm = akkaModule("stm")
    val akkaTypedActor = akkaModule("typed-actor")
    val akkaRemote = akkaModule("remote")
    val akkaHttp = akkaModule("http")
    val akkaAmqp = akkaModule("amqp")
    val akkaCamel = akkaModule("camel")
    val akkaSpring = akkaModule("spring")
    val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"
    val specs = "org.specs2" % "specs2_2.9.0.RC1" % "1.2-SNAPSHOT" % "test"
}
