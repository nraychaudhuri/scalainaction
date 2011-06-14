import sbt._

class ScalaHibernateProjectInfo(info: ProjectInfo) extends DefaultProject(info) {
  
  val hibernate = "org.hibernate" % "hibernate" % "3.1.2"
}