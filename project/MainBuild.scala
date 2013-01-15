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
  val completeSbtProjectStructure = Project("complete-sbt-project-structure", file("chap06/complete_sbt_project_structure"))
  
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
      completeSbtProjectStructure)
}