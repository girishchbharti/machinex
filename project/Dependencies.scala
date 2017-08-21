import sbt.{Def, _}

object Dependencies {

  lazy val akkaVersion = "2.4.0"

  val emailVersion = "1.4"
  val json4sVersion = "3.5.0"
  val scalaTestVersion = "2.2.2"

  def compileDependencies(deps: List[ModuleID]): Seq[ModuleID] = deps map (_ % Compile)

  def providedDependencies(deps: List[ModuleID]): Seq[ModuleID] = deps map (_ % Provided)

  def testDependencies(deps: List[ModuleID]): Seq[ModuleID] = deps map (_ % Test)

  def testClassifierDependencies(deps: List[ModuleID]): Seq[ModuleID]  = deps map (_ % "test" classifier "tests")

  /**
    * ********************************
    * DEPENDENCIES                   *
    * ********************************
    */
  def akkaActor: Def.Initialize[List[ModuleID]] = Def.setting {
    "com.typesafe.akka" %% "akka-actor" % akkaVersion :: Nil
  }

  def logbackClassic: Def.Initialize[List[ModuleID]] = Def.setting {
    "ch.qos.logback" % "logback-classic" % "1.2.3" :: Nil
  }

  def log4j: Def.Initialize[List[ModuleID]] = Def.setting {
    "log4j" % "log4j" % "1.2.17" :: Nil
  }

  def json4sNative: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.json4s" %% "json4s-native" % json4sVersion :: Nil
  }

  def breez: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.scalanlp" %% "breeze" % "0.13.2" :: Nil
  }

  def breezNative: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.scalanlp" %% "breeze-natives" % "0.13.2" :: Nil
  }

  def breezViz: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.scalanlp" %% "breeze-viz" % "0.13.2" :: Nil
  }

  /**
    * ********************************
    * TEST DEPENDENCIES              *
    * ********************************
    */
  def akkaTestKit: Def.Initialize[List[ModuleID]] = Def.setting {
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion :: Nil
  }

  /*def scalaTest: Def.Initialize[List[ModuleID]] = Def.setting {
    "org.scalatest" %% "scalatest" % "2.2.2" :: Nil
  }*/


}
