import sbt._
import Keys._

object ExampleBuild extends Build {
  val hello = TaskKey[Unit]("hello", "Prints 'Hello World'")

  val helloTask = hello := {
    println("Hello World")
  }

  lazy val project = Project (
    "project",
    file (".")) settings(helloTask)
}