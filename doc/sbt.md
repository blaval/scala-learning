# Learning SBT build tool

Documentation: sbt [1.6.2](https://www.scala-sbt.org/1.x/docs/Custom-Settings.html)

Github repo: [here](https://github.com/sbt/sbt)

## Table of content

1. [Useful commands](#useful-commands)
    1. [In bash mode](#in-bash-mode)
    2. [In server mode](#in-server-mode)
2. [build.sbt](#buildsbt)
3. [sbt Keys](#sbt-keys)
4. [Getting started](#getting-started)
5. [Troubleshooting](#troubleshooting)

## Useful commands

### In bash mode

```bash
sbt clean scalafmtAll scalafmtSbt Test/compile IntegrationTest/compile
sbt clean compile test:compile it:compile
sbt test
sbt IntegrationTest/test
```

### In server mode

While `sbt` is running

```bash
# (Ctrl-R incrementally searches the history backwards)
reload # reload conf if any changes in build.sbt
update # resolve and optionally download dependencies to the coursier cache, producing a report
! # show history command help
help <task> # e.g help dist 
show <task> # displays the output of the task
inspect <task> # returns info on tasks or settings
inspect tree <task> # generate the dependencies tree of the task
console # run scala interpreter (REPL)
plugins # lists auto plugins (enabled and not enabled) per subprojects
last <task> # see the previous output of a command at a higher verbosity

~<task> # to automatically rerun a task, e.g ~run
~testQuick # automatically rerun failed tests
run <argument>* # run the main class in the current subproject (root by default)

++2.12.14! # to change scalaVersion temporarily (will be reset after running reload)
set ThisBuild / scalaVersion := "2.13.6" # update .sbt configuration but only for the time of sbt session
session save # override build.sbt file with the conf of the current session

projects # list all subprojects handled in sbt
project <projectName> # set <projectName> as current project
<projectName>/compile # run compile for a specific <projectName>

dist # create a distribution package
  # to execute the packaged app
  mkdir <testsomedistribution>
  cd testsomedistribution
  unzip -o -d . ../target/universal/<projectName>.zip
  ./<projectName>/bin/<executableName>

Docker/publishLocal # create a docker image locally
```

## build.sbt

Example with various settings illustrating some key concepts

```scala
inThisBuild(Seq(version := "0.1.0-SNAPSHOT", scalaVersion := "2.13.8", organization := "blaval"))

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"
val gigahorse = "com.eed3si9n" %% "gigahorse-okhttp" % "0.6.0"
val playJson = "com.typesafe.play" %% "play-json" % "2.9.2"

lazy val ScalaLearning = (project in file("."))
  .aggregate(ScalaLearningCore)
  .dependsOn(ScalaLearningCore)
  .enablePlugins(JavaAppPackaging)
  .disablePlugins(plugins.JUnitXmlReportPlugin)
  .settings(
    name := "scala-learning",
    Compile / mainClass := (ScalaLearningCore / Compile / mainClass).value,
    libraryDependencies += scalaTest % Test
  )

lazy val taskExample = taskKey[Unit]("re-run each time the task is executed")
lazy val taskExample2 = taskKey[String]("re-run each time the task is executed")

lazy val ScalaLearningCore = (project in file("modules/core"))
  .settings(
    name := "scala-learning-core",
    Compile / scalaSource := {
      val current = (Compile / scalaSource).value // .value is executed before everything in this task
      scalaBinaryVersion.value match {
        case "2.11" => baseDirectory.value / "src-2.11" / "main" / "scala"
        case "2.12" => baseDirectory.value / "src-2.12" / "main" / "scala"
        case _ => current
      }
    },
    taskExample := {
      println(s"Current project version is ${version.value}")
    },
    taskExample2 := {
      s"Current project version is ${version.value}"
    },
    libraryDependencies ++= Seq(gigahorse, playJson),
    libraryDependencies += scalaTest % Test
  )
  .dependsOn(ScalaLearningModel % "test->test;compile->compile")

lazy val ScalaLearningModel = (project in file("modules/models"))
  .settings(name := "scala-learning-models", libraryDependencies += scalaTest % Test)
```

## sbt Keys

Description of all [keys](https://github.com/sbt/sbt/blob/develop/main/src/main/scala/sbt/Keys.scala).

[Default values](https://github.com/sbt/sbt/blob/develop/main/src/main/scala/sbt/Defaults.scala) of the keys at runtime.

Common keys:

```bash
libraryDependencies # Declares managed dependencies
unmanagedBase # The default directory for manually managed libraries
unmanagedJars # Classpath entries for the current project (shallow) that are manually managed
compile:dependencyClasspath # The classpath consisting of internal and external, managed and unmanaged dependencies
resolvers # The user-defined additional resolvers for automatically managed dependencies
externalResolvers # The external resolvers for automatically managed dependencies
```

## Getting started

1. `build.sbt` implicitly imports
   ```bash
   import sbt._
   import Keys._
   ```
2. Each subproject in a build has its own source directories, generates its own jar file when you run package,
3. Aggregation means that running a task on the subproject will also run it on the aggregated subprojects
4. A project may depend on code in another project. This is done by adding a `<project>.dependsOn` method to your
   project
    1. core dependsOn(util) means that the compile configuration in core depends on the compile configuration in util.
       You could write this explicitly as dependsOn(util % "compile->compile").
    2. a useful declaration is "test->test" which means test depends on test. This allows you to put utility code for
       testing in **util**/src/test/scala and then use that code in **core**/src/test/scala, for example.
    3. you can have multiple configurations for a dependency, separated by semicolons. For example, dependsOn(util % "
       test->test;compile->compile").
5. Task
    1. for tasks: whether `.value` calls are inlined, or placed anywhere in the task body, they are still evaluated
       before entering the task body.
    2. when a task is invoked by you, sbt’s tasks engine will:
        1. evaluate the task dependencies before evaluating the task body (partial ordering)
        2. try to evaluate task dependencies in parallel if they are independent (parallelization)
        3. each task dependency will be evaluated once and only once per command execution (deduplication)
    3. a setting key can’t depend on a task key. That’s because a setting key is only computed once on project load, so
       the task would not be re-run every time, and tasks expect to re-run every time.
6. Scoped key
    1. there is a single value for a given scoped key.
    2. a full scope in sbt is formed by a tuple of a subproject, a configuration, and a task value:
       `projA / Compile / console / scalacOptions`
        1. e.g ref / Config / intask / key
        2. ref identifies the subproject axis. It could be <project-id>, ProjectRef(uri("file:..."), "id"), or ThisBuild
           that denotes the “entire build” scope.
    3. a scope is a tuple of components in three axes: the subproject axis, the configuration axis, and the task axis.
    4. there’s a special scope component Zero for any of the scope axes.
    5. there’s a special scope component ThisBuild for the subprojects axis only.
    6. `Test` extends `Runtime`, and `Runtime` extends `Compile` configuration.
    7. a key placed in build.sbt is scoped to ${current subproject} / Zero / Zero by default.
    8. To change the value associated with the compile key, you need to write `Compile / compile` or `Test / compile`.
       Using plain compile would define a new compile task scoped to the current project, rather than overriding the
       standard compile tasks which are scoped to a specific configuration.
    9. if a key that is scoped to a particular subproject is not found, sbt will look for it in `ThisBuild` as a
       fallback (`ThisBuild / <key>`) or
       `inThisBuild(Seq(version := "0.1", scalaVersion := "2.13.8", organization := "blaval"))`
7. You can compute values of some tasks or settings to define or append a value for another task. It’s done by using
   Def.task as an argument to :=, +=, or ++=. As an example, consider appending a source generator using the project
   base directory and compilation classpath.
   `Compile / sourceGenerators += Def.task {myGenerator(baseDirectory.value, (Compile / managedClasspath).value)}`
8. docker run <nameProvidedByPublishLocalCmd>
9. [Library dependencies](https://www.scala-sbt.org/1.x/docs/Library-Dependencies.html#Library+dependencies)
    1. unmanaged dependencies
        1. add jars to `/lib` and they will be placed on the project classpath
        2. check `unmanagedBase` setting for `/lib` location
            * To use `/custom_lib` instead of `/lib`:
           ```
           unmanagedBase := baseDirectory.value / "custom_lib"
           ```
        3. `unmanagedJars` setting lists the jars from the `unmanagedBase` directory
    2. managed dependencies
       ```bash
       val libraryDependencies = settingKey[Seq[ModuleID]]("Declares managed dependencies.")
       libraryDependencies += groupID % artifactID % revision % configuration # configuration such as Test or "test"
       # examples
       libraryDependencies += "org.scala-tools" % "scala-stm_2.11" % "0.3"
       libraryDependencies += "org.scala-tools" %% "scala-stm" % "0.3"
       ```
       Resolvers: If your dependency isn’t on one of the default repositories, you’ll have to add a resolver to help Ivy
       find it.
       ```bash
       # only additional resolvers added by your build definition
       val resolvers = settingKey[Seq[Resolver]]("The user-defined additional resolvers for automatically managed dependencies.")
       resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
       resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
       resolvers += Resolver.mavenLocal # for convenience
       ```
       to override default resolvers, update `externalResolvers` setting
10. Using plugins
    1. [best practices](https://www.scala-sbt.org/1.x/docs/Best-Practices.html#global-vs-local-plugins)
    2. in `<subproject>/project`
        1. add `addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.7.0")` in `plugins.sbt`
        2. add `addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")` in `assembly.sbt`
    3. auto plugins (since sbt 0.13.5)
        1. in `build.sbt` add
       ```bash
       .enablePlugins(FooPlugin, BarPlugin) # if explicit enablement required
       .disablePlugins(plugins.IvyPlugin) # to exclude some plugins
       ```
       notes:
        1. CorePlugin: Provides the core parallelism controls for tasks.
        2. IvyPlugin: Provides the mechanisms to publish/resolve modules.
        3. JvmPlugin: Provides the mechanisms to compile/test/run/package Java/Scala projects.
        4. JUnitXmlReportPlugin: experimental support for generating junit-xml
    4. plugins can be installed for all your projects at once by declaring them in
       `$HOME/.sbt/1.0/plugins/`. `$HOME/.sbt/1.0/plugins/` is an sbt project whose classpath is exported to all sbt
       build definition projects. Roughly speaking, any `.sbt` or `.scala`
       files in `$HOME/.sbt/1.0/plugins/` behave as if they were in the `project/` directory for all projects.
11. Custom settings
    1. Keys may be defined in an `.sbt` file, a `.scala` file, or in an auto plugin. Any vals found under `autoImport`
       object of an enabled auto plugin will be imported automatically into your `.sbt` files.
    2. convenient APIs in [sbt IO](https://www.scala-sbt.org/1.x/api/sbt/io/IO$.html) to manipulate files and
       directories.
    3. Example of defining tasks ordering (so that deduplication is auto-performed by sbt)
       ```scala
       lazy val library = (project in file("library"))
         .settings(
           startServer := {
           println("starting...")
           Thread.sleep(500)
         },
         sampleIntTask := {
           startServer.value
           val sum = 1 + 2
           println("sum: " + sum)
           sum
         },
         sampleStringTask := {
           startServer.value
           val s = sampleIntTask.value.toString
           println("s: " + s)
           s
         },
         sampleStringTask := {
           val old = sampleStringTask.value
           println("stopping...")
           Thread.sleep(500)
           old
         }
         )
       ```
    4. Or you can use plain Scala code to define your logic (put in `project/<className>.scala`) then import it
       to `build.sbt` and use it in tasks but **deduplication will not be performed!**
    5. If you have a lot of custom code, consider moving it to a plugin for re-use across multiple builds
12. Organizing the build
    1. sbt is recursive:
        1. the project directory is another build inside your build, which knows how to build your build.
        2. each project folder can have its own project folder used to define the build of its project folder
    2. good practice: put all Dependencies in `project/Dependencies.scala` and import it in your `build.sbt`

## Troubleshooting

1. Do not open another terminal to run more than 1 sbt shell server on the same project, you may get
   ```
   sbt thinks that server is already booting because of this exception:
   sbt.internal.ServerAlreadyBootingException: java.io.IOException: Could not create lock for \\.\pipe\sbt-load-8979016220299674466_lock, error 5
           at sbt.internal.BootServerSocket.newSocket(BootServerSocket.java:348)
   ```
   **workaround:** Restart Intellij to remove the locks. If you run `sbt with -Dsbt.server.forcestart=true`
   this behavior should be avoided (you can add this to `.sbtopts` in the project directory to always apply the
   property).