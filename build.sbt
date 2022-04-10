inThisBuild(Seq(version := "0.1.0-SNAPSHOT", scalaVersion := "2.13.8", organization := "blaval"))

val scalaTest = "org.scalatest"     %% "scalatest"        % "3.2.11"
val gigahorse = "com.eed3si9n"      %% "gigahorse-okhttp" % "0.6.0"
val playJson  = "com.typesafe.play" %% "play-json"        % "2.9.2"

lazy val ScalaLearning = (project in file("."))
  .aggregate(scalaLearningCore, scalaLearningCats, scalaLearningDecline)
  .dependsOn(scalaLearningCore)
  .enablePlugins(JavaAppPackaging)
  .disablePlugins(plugins.JUnitXmlReportPlugin)
  .settings(
    name                            := "scala-learning",
    Compile / mainClass             := (scalaLearningCore / Compile / mainClass).value,
    libraryDependencies += scalaTest % Test,
    publish                         := {},
    publish / skip                  := true
  )

lazy val taskExample  = taskKey[Unit]("re-run each time the task is executed")
lazy val taskExample2 = taskKey[String]("re-run each time the task is executed")

lazy val scalaLearningCore = (project in file("modules/core"))
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
  .dependsOn(scalaLearningModel % "test->test;compile->compile")

lazy val scalaLearningModel = (project in file("modules/models"))
  .settings(name := "scala-learning-models", libraryDependencies += scalaTest % Test)

lazy val scalaLearningCats = (project in file("modules/cats"))
  .settings(
    name := "scala-learning-cats",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"         % "3.3.0",
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.7"
      //  "org.typelevel" %% "cats-mtl" % "1.1.2.1"
    )
  )

val nativeImageSettings: Seq[Setting[_]] = Seq(
  nativeImageVersion := "21.2.0",
  nativeImageOptions ++= Seq(
    s"-H:ReflectionConfigurationFiles=${(Compile / resourceDirectory).value / "reflect-config.json"}",
    s"-H:ResourceConfigurationFiles=${(Compile / resourceDirectory).value / "resource-config.json"}",
    "-H:+ReportExceptionStackTraces",
    "--no-fallback",
    "--allow-incomplete-classpath",
    "-H:-CheckToolchain"
  ),
  nativeImageAgentMerge := true,
  nativeImageReady      := { () => println("IT IS READY!!!!!") }
)

lazy val scalaLearningDecline = (project in file("modules/decline"))
  .settings(
    name                := "scala-learning-decline",
    Compile / mainClass := Some("blaval.DeclineCommandIOApp"),
    run / fork          := true,
    libraryDependencies ++= Seq("com.monovore" %% "decline" % "2.2.0", "com.monovore" %% "decline-effect" % "2.2.0"),
    nativeImageSettings,
    publish        := {},
    publish / skip := true
  )
  .enablePlugins(NativeImagePlugin)
