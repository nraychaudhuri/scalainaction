import sbt._

object MainBuild extends Build {
  //chapter1
  val chapter1Examples = Project("chap01-examples", file("chap01"))
  //chapter 2
  val restClient = Project("chap02-rest-client", file("chap02/restclient"))
  //chapter 3
  val chapter3Examples = Project("chap03-examples", file("chap03"))  
  val scalaMongoDriver = Project("chap03-scala-mongo-driver", file("chap03/scala-mongo-driver-sbt"))  
  //chapter 4
  val chapter4Examples = Project("chap04-examples", file("chap04"))  
  //chapter 5
  val fpExamples = Project("chap05-fp-examples", file("chap05/fp-examples"))
  val liftingExamples = Project("chap05-lifting", file("chap05/lifting"))
  val monads = Project("chap05-monads", file("chap05/monads"))
  val nanoHttpServer = Project("chap05-nano-http-server", file("chap05/nano-http-server"))

  //chapter 6
  val completeSbtProjectStructure = Project("chap06-complete-sbt-project-structure", file("chap06/complete_sbt_project_structure"))

  //chapter 8
  val buildingBlocks = Project("chap08-building-blocks", file("chap08/buildingBlocks"))
  val orderingSystem = Project("chap08-ordering-system", file("chap08/orderingSystem"))
  val typeclasses = Project("chap08-typeclasses", file("chap08/typeclasses"))
  
  //chapter 9
  val wordCount = Project("chap09-word-count", file("chap09/word_count"))
  val chapter9Examples = Project("chap09-examples", file("chap09"))
  val askPipePatternExample = Project("chap09-ask-pipe-pattern", file("chap09/ask-pipe-pattern-example"))
  val supervisorExample = Project("chap09-supervisor-example", file("chap09/supervisor_example"))
  val wordCountWithFuture = Project("chap09-word-count-future", file("chap09/word_count_future"))

  //chapter 10
  val actorTest = Project("chap10-actor-test", file("chap10/actor-test"))
  val di = Project("chap10-di", file("chap10/di"))
  val scalacheck = Project("chap10-scalacheck", file("chap10/scalacheck"))
  
  //chapter 11
  val javaInScala = Project("chap11-java-in-scala", file("chap11/java_in_scala"))
  val scalaInJava = Project("chap11-scala-in-java", file("chap11/scala_in_java"))

  //chapter 12
  val agentExample = Project("chap12-agent-example", file("chap12/agent-example"))
  val dataflowExample = Project("chap12-dataflow-example", file("chap12/dataflow-example"))
  val stmExample = Project("chap12-stm-example", file("chap12/stm-example"))
  val wordCountRemoteAkka = Project("chap12-word-count-remote-example", file("chap12/word-count-remote-akka"))
  
  val main = Project("scala-in-action", file("."))
    .aggregate(
      chapter1Examples,
      restClient, 
      chapter3Examples,
      scalaMongoDriver, 
      chapter4Examples, 
      fpExamples, 
      liftingExamples,
      monads,
      nanoHttpServer,
      completeSbtProjectStructure,
      buildingBlocks,
      orderingSystem,
      typeclasses,
      wordCount,
      chapter9Examples,
      askPipePatternExample,
      supervisorExample,
      wordCountWithFuture,
      actorTest,
      di,
      scalacheck,
      javaInScala,
      scalaInJava,
      agentExample,
      dataflowExample,
      stmExample,
      wordCountRemoteAkka)
}