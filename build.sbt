inThisBuild(Seq(version := "0.1.0-SNAPSHOT", scalaVersion := "2.13.8", organization := "blaval"))

val scalaTest = "org.scalatest"     %% "scalatest"        % "3.2.11"
val gigahorse = "com.eed3si9n"      %% "gigahorse-okhttp" % "0.6.0"
val playJson  = "com.typesafe.play" %% "play-json"        % "2.9.2"

lazy val ScalaLearning = (project in file("."))
  .aggregate(ScalaLearningCore)
  .dependsOn(ScalaLearningCore)
  .enablePlugins(JavaAppPackaging)
  .disablePlugins(plugins.JUnitXmlReportPlugin)
  .settings(
    name                            := "scala-learning",
    Compile / mainClass             := (ScalaLearningCore / Compile / mainClass).value,
    libraryDependencies += scalaTest % Test
  )

lazy val taskExample  = taskKey[Unit]("re-run each time the task is executed")
lazy val taskExample2 = taskKey[String]("re-run each time the task is executed")

lazy val ScalaLearningCore = (project in file("modules/core"))
  .settings(
    name := "scala-learning-core",
    Compile / scalaSource := {
      val current = (Compile / scalaSource).value // .value is executed before everything in this task
      scalaBinaryVersion.value match {
        case "2.11" => baseDirectory.value / "src-2.11" / "main" / "scala"
        case "2.12" => baseDirectory.value / "src-2.12" / "main" / "scala"
        case _      => current
      }
    },
    taskExample  := { println(s"Current project version is ${version.value}") },
    taskExample2 := { s"Current project version is ${version.value}" },
    libraryDependencies ++= Seq(gigahorse, playJson),
    libraryDependencies += scalaTest % Test
  )
  .dependsOn(ScalaLearningModel % "test->test;compile->compile")

lazy val ScalaLearningModel = (project in file("modules/models"))
  .settings(name := "scala-learning-models", libraryDependencies += scalaTest % Test)
