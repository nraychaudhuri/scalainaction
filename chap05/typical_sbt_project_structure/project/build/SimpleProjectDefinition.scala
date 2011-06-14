import sbt._

class SimpleProjectDefinition(info: ProjectInfo) extends DefaultProject(info)
{
  override def mainClass = Some("HelloWorld")
  override def compileOptions = super.compileOptions ++ Seq(Unchecked)
  //Runtime dependencies
  val jettyServer = "org.eclipse.jetty" % "jetty-server" % "7.0.0.RC2"
  //Test dependencies
  val specs = "org.scala-tools.testing" % "specs" % "1.6.2" % "test" 
}