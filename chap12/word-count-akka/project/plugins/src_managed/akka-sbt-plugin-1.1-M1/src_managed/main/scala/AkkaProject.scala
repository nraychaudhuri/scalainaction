import sbt._

object AkkaRepositories {
  lazy val Akka_Repository = MavenRepository("Akka Repository", "http://akka.io/repository")
  lazy val Scala_Tools_Maven__Snapshots_Repository = MavenRepository("Scala-Tools Maven2 Snapshots Repository", "http://scala-tools.org/repo-snapshots")
  lazy val Sun_JDMK_Repo = MavenRepository("Sun JDMK Repo", "http://wp5.e-taxonomy.eu/cdmlib/mavenrepo")
  lazy val JBoss_Repo = MavenRepository("JBoss Repo", "http://repository.jboss.org/nexus/content/groups/public/")
  lazy val Scala_Tools_Repo = MavenRepository("Scala-Tools Repo", "http://scala-tools.org/repo-releases")
  lazy val java_net_Repo = MavenRepository("java.net Repo", "http://download.java.net/maven/2")
  lazy val GuiceyFruit_Repo = MavenRepository("GuiceyFruit Repo", "http://guiceyfruit.googlecode.com/svn/repo/releases/")
  lazy val public = MavenRepository("public", "http://repo1.maven.org/maven2/")
  lazy val Codehaus_Repo = MavenRepository("Codehaus Repo", "http://repository.codehaus.org")
}

trait AkkaBaseProject extends BasicScalaProject {
  import AkkaRepositories._

  lazy val org_scannotation = ModuleConfiguration("org.scannotation", "*", "*", JBoss_Repo)
  lazy val com_sun_jersey_contribs = ModuleConfiguration("com.sun.jersey.contribs", "*", "*", java_net_Repo)
  lazy val org_multiverse = ModuleConfiguration("org.multiverse", "*", "*", Codehaus_Repo)
  lazy val org_jboss = ModuleConfiguration("org.jboss", "*", "*", JBoss_Repo)
  lazy val org_eclipse_jetty = ModuleConfiguration("org.eclipse.jetty", "*", "*", public)
  lazy val javax_jms = ModuleConfiguration("javax.jms", "*", "*", Sun_JDMK_Repo)
  lazy val com_rabbitmq_rabbitmq_client_0_9_1 = ModuleConfiguration("com.rabbitmq", "rabbitmq-client", "0.9.1", Akka_Repository)
  lazy val net_debasishg = ModuleConfiguration("net.debasishg", "*", "*", Scala_Tools_Repo)
  lazy val org_scala_tools_testing_scalacheck_2_9_0_RC1_1_9_SNAPSHOT = ModuleConfiguration("org.scala-tools.testing", "scalacheck_2.9.0.RC1", "1.9-SNAPSHOT", Scala_Tools_Maven__Snapshots_Repository)
  lazy val com_sun_jersey = ModuleConfiguration("com.sun.jersey", "*", "*", java_net_Repo)
  lazy val com_atomikos = ModuleConfiguration("com.atomikos", "*", "*", public)
  lazy val org_scala_tools_time = ModuleConfiguration("org.scala-tools", "time", "*", Scala_Tools_Repo)
  lazy val args_j = ModuleConfiguration("args4j", "*", "*", JBoss_Repo)
  lazy val org_scalatest_scalatest_1_4_SNAPSHOT = ModuleConfiguration("org.scalatest", "scalatest", "1.4-SNAPSHOT", Scala_Tools_Maven__Snapshots_Repository)
  lazy val voldemort_store_compress_h2_lzf = ModuleConfiguration("voldemort.store.compress", "h2-lzf", "*", Akka_Repository)
  lazy val org_guiceyfruit = ModuleConfiguration("org.guiceyfruit", "*", "*", GuiceyFruit_Repo)
  lazy val com_sun_jmx = ModuleConfiguration("com.sun.jmx", "*", "*", Sun_JDMK_Repo)
  lazy val com_sun_jdmk = ModuleConfiguration("com.sun.jdmk", "*", "*", Sun_JDMK_Repo)
  lazy val org_codehaus_aspectwerkz_aspectwerkz_2_2_3 = ModuleConfiguration("org.codehaus.aspectwerkz", "aspectwerkz", "2.2.3", Akka_Repository)
  lazy val org_scalaz = ModuleConfiguration("org.scalaz", "*", "*", Scala_Tools_Maven__Snapshots_Repository)
  lazy val org_jboss_netty = ModuleConfiguration("org.jboss.netty", "*", "*", JBoss_Repo)
}

trait AkkaProject extends AkkaBaseProject {
  val akkaVersion = "1.1-M1"
  val akkaModulesVersion = "1.1-M1"

  def akkaModule(module: String) = "se.scalablesolutions.akka" % ("akka-" + module) % {
    if (Set("scalaz", "camel", "dispatcher-extras", "osgi", "samples", "kernel", "spring", "camel-typed", "amqp").contains(module))
      akkaModulesVersion
    else
      akkaVersion
  }

  val akkaActor = akkaModule("actor")
}
