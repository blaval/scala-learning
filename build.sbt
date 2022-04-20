inThisBuild(Seq(version := "0.1.0-SNAPSHOT", scalaVersion := "2.13.8", organization := "blaval"))

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"

lazy val ScalaLearning = (project in file("."))
  .aggregate(ScalaLearningCore)
  .dependsOn(ScalaLearningCore)
  .settings(
    name                            := "scala-learning",
    Compile / mainClass             := (ScalaLearningCore / Compile / mainClass).value,
    libraryDependencies += scalaTest % Test
  )

lazy val ScalaLearningCore = (project in file("modules/core"))
  .settings(name := "scala-learning-core", libraryDependencies += scalaTest % Test)
  .dependsOn(ScalaLearningModel)

lazy val ScalaLearningModel = (project in file("modules/models"))
  .settings(name := "scala-learning-models", libraryDependencies += scalaTest % Test)
