import sbt._

object AkkaRepositories {
  lazy val Akka_Repository = MavenRepository("Akka Repository", "http://akka.io/repository")
  lazy val Sun_JDMK_Repo = MavenRepository("Sun JDMK Repo", "http://wp5.e-taxonomy.eu/cdmlib/mavenrepo")
  lazy val JBoss_Repo = MavenRepository("JBoss Repo", "http://repository.jboss.org/nexus/content/groups/public/")
  lazy val GuiceyFruit_Repo = MavenRepository("GuiceyFruit Repo", "http://guiceyfruit.googlecode.com/svn/repo/releases/")
  lazy val Scala_Tools_Repo = MavenRepository("Scala-Tools Repo", "http://scala-tools.org/repo-releases")
  lazy val java_net_Repo = MavenRepository("java.net Repo", "http://download.java.net/maven/2")
  lazy val public = MavenRepository("public", "http://repo1.maven.org/maven2/")
  lazy val Codehaus_Repo = MavenRepository("Codehaus Repo", "http://repository.codehaus.org")
}

trait AkkaBaseProject extends BasicScalaProject {
  import AkkaRepositories._

  lazy val org_jboss_netty = ModuleConfiguration("org.jboss.netty", "*", "*", JBoss_Repo)
  lazy val org_scala_tools_testing_scalacheck_2_9_0_1_1_9 = ModuleConfiguration("org.scala-tools.testing", "scalacheck_2.9.0-1", "1.9", Scala_Tools_Repo)
  lazy val com_sun_jersey_contribs = ModuleConfiguration("com.sun.jersey.contribs", "*", "*", java_net_Repo)
  lazy val org_jboss = ModuleConfiguration("org.jboss", "*", "*", JBoss_Repo)
  lazy val org_multiverse = ModuleConfiguration("org.multiverse", "*", "*", Codehaus_Repo)
  lazy val javax_jms = ModuleConfiguration("javax.jms", "*", "*", Sun_JDMK_Repo)
  lazy val org_eclipse_jetty = ModuleConfiguration("org.eclipse.jetty", "*", "*", public)
  lazy val org_springframework = ModuleConfiguration("org.springframework", "*", "*", public)
  lazy val net_debasishg = ModuleConfiguration("net.debasishg", "*", "*", Scala_Tools_Repo)
  lazy val com_rabbitmq_rabbitmq_client_0_9_1 = ModuleConfiguration("com.rabbitmq", "rabbitmq-client", "0.9.1", Akka_Repository)
  lazy val com_sun_jersey = ModuleConfiguration("com.sun.jersey", "*", "*", java_net_Repo)
  lazy val com_atomikos = ModuleConfiguration("com.atomikos", "*", "*", public)
  lazy val org_scala_tools_time = ModuleConfiguration("org.scala-tools", "time", "*", Scala_Tools_Repo)
  lazy val args_j = ModuleConfiguration("args4j", "*", "*", JBoss_Repo)
  lazy val org_scalatest_scalatest_1_4_1 = ModuleConfiguration("org.scalatest", "scalatest", "1.4.1", Scala_Tools_Repo)
  lazy val org_scalaz = ModuleConfiguration("org.scalaz", "*", "*", Scala_Tools_Repo)
  lazy val javax_ws_rs = ModuleConfiguration("javax.ws.rs", "*", "*", java_net_Repo)
  lazy val org_guiceyfruit = ModuleConfiguration("org.guiceyfruit", "*", "*", GuiceyFruit_Repo)
  lazy val com_sun_jmx = ModuleConfiguration("com.sun.jmx", "*", "*", Sun_JDMK_Repo)
  lazy val se_scalablesolutions_akka = ModuleConfiguration("se.scalablesolutions.akka", "*", "*", Akka_Repository)
  lazy val voldemort_store_compress_h2_lzf = ModuleConfiguration("voldemort.store.compress", "h2-lzf", "*", Akka_Repository)
  lazy val com_sun_jdmk = ModuleConfiguration("com.sun.jdmk", "*", "*", Sun_JDMK_Repo)
  lazy val org_codehaus_aspectwerkz_aspectwerkz_2_2_3 = ModuleConfiguration("org.codehaus.aspectwerkz", "aspectwerkz", "2.2.3", Akka_Repository)
  lazy val commons_codec = ModuleConfiguration("commons-codec", "*", "*", public)
  lazy val org_scannotation = ModuleConfiguration("org.scannotation", "*", "*", JBoss_Repo)
}

trait AkkaProject extends AkkaBaseProject {
  val akkaVersion = "1.1.2"
  val akkaModulesVersion = "1.1.2"

  def akkaModule(module: String) = "se.scalablesolutions.akka" % ("akka-" + module) % {
    if (Set("scalaz", "camel", "dispatcher-extras", "modules-samples", "kernel", "dist", "spring", "camel-typed", "amqp").contains(module))
      akkaModulesVersion
    else
      akkaVersion
  }

  val akkaActor = akkaModule("actor")
}
