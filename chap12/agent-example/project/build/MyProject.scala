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
    val akkaJta = akkaModule("jta")
    val scalaToolsSnapshots = "Scala Tools Snapshots" at  "http://scala-tools.org/repo-snapshots/"

    val specs2 = "org.specs2" %% "specs2" % "1.0.1"

    def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
    override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
}
