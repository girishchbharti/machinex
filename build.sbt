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
  libraryDependencies ++= compileDependencies(akkaActor.value ++ breez.value)
    ++ testDependencies(Nil)
    ++ testClassifierDependencies(Nil/*scalaTest.value*/),
  parallelExecution in Test := false).dependsOn(common, persistence)

lazy val persistence = BaseProject("persistence").settings(
  libraryDependencies ++= compileDependencies(Nil)
    ++ testDependencies(Nil/*scalaTest.value*/),
  parallelExecution in Test := false).dependsOn(common)

lazy val common = BaseProject("common").settings(
  libraryDependencies ++= compileDependencies(logbackClassic.value ++ log4j.value ++ json4sNative.value)
    ++ testDependencies(Nil/*scalaTest.value*/),
  parallelExecution in Test := false)

lazy val examples = BaseProject("examples").settings(
  libraryDependencies ++= compileDependencies(Nil)
    ++ testDependencies(Nil),
  parallelExecution in Test := false).dependsOn(learning)
