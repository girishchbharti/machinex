name := """machinex"""

version := "1.0"

scalaVersion := "2.11.6"


import Dependencies._
import ProjectSettings._

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

lazy val learning = BaseProject("learning").settings(
  libraryDependencies ++= compileDependencies(Nil/*akkaHttp.value ++ slf4j.value ++ log4j.value ++ logback.value ++
    json4sNative.value ++ json4sEx.value ++ jodaDate.value ++ kafka.value*/)
    ++ testDependencies(Nil)
    ++ testClassifierDependencies(Nil),
  parallelExecution in Test := false).dependsOn(common, persistence)


lazy val persistence = BaseProject("persistence").settings(
  libraryDependencies ++= compileDependencies(Nil)
    ++ testDependencies(Nil),
  parallelExecution in Test := false).dependsOn(common)

lazy val common = BaseProject("common").settings(
  libraryDependencies ++= compileDependencies(Nil)
    ++ testDependencies(Nil),

  parallelExecution in Test := false
)

/*

lazy val akkaVersion = "2.4.0"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "compile",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "compile",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "compile",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "compile",
  "log4j" % "log4j" % "1.2.17" % "compile",
  "org.scalanlp" %% "breeze" % "0.13.2" % "compile",
  "org.scalanlp" %% "breeze-natives" % "0.13.2" % "compile",
  "org.scalanlp" %% "breeze-viz" % "0.13.2" % "compile"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
*/
