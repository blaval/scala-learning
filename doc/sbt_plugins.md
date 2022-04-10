# Useful sbt plugins

## Table of content

1. [Plugins configuration](#plugins-configuration)
    1. [in plugins file](#in-plugins-file)
    2. [in build.sbt](#in-buildsbt)
2. [Plugins description](#plugins-description)
    1. [sbt-dependency-graph](#sbt-dependency-graph)
    2. [sbt-github-actions](#sbt-github-actions)
    3. [sbt-native-packager](#sbt-native-packager)
    4. [sbt-tpolecat](#sbt-tpolecat)

## Plugins configuration

### in plugins file

```scala
// build application packages in native formats. for autoplugins https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
// automagically configuring scalac options according to the project Scala version https://github.com/DavidGregory084/sbt-tpolecat
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")
// assisting in building sbt projects using GitHub Actions https://github.com/djspiewak/sbt-github-actions
addSbtPlugin("com.codecommit" % "sbt-github-actions" % "0.14.2")
```

For sbt < 1.3

```scala
// Visualize your project's dependencies https://github.com/sbt/sbt-dependency-graph
addDependencyTreePlugin // For sbt > 1.4
// or
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1") // For sbt < 1.3
```

### in build.sbt

## Plugins description

### [sbt-dependency-graph](https://github.com/sbt/sbt-dependency-graph)

| task | description |
|------|-------------|
|`dependencyTree`|Shows an ASCII tree representation of the project's dependencies|
|`dependencyBrowseGraph`|Opens a browser window with a visualization of the dependency graph (courtesy of graphlib-dot + dagre-d3).|
|`dependencyBrowseTree`|Opens a browser window with a visualization of the dependency tree (courtesy of jstree).|
|`dependencyList`|Shows a flat list of all transitive dependencies on the sbt console (sorted by organization and name)|
|`whatDependsOn` `<organization> <module> <revision>?`| Find out what depends on an artifact. Shows a reverse dependency tree for the selected module. The <revision> argument is optional.|
|`dependencyLicenseInfo`| show dependencies grouped by declared license|
|`dependencyStats`| Shows a table with each module a row with (transitive) Jar sizes and number of dependencies|
|`dependencyGraphMl`| Generates a .graphml file with the project's dependencies to target/dependencies-<config>.graphml. Use e.g. yEd to format the graph to your needs.|
|`dependencyDot`| Generates a .dot file with the project's dependencies to target/dependencies-<config>.dot. Use graphviz to render it to your preferred graphic format.|
|`dependencyGraph`| Shows an ASCII graph of the project's dependencies on the sbt console (only supported on sbt 0.13)|
|`ivyReport`| Lets ivy generate the resolution report for you project. Use show ivyReport for the filename of the generated report|

### [sbt-github-actions](https://github.com/djspiewak/sbt-github-actions)

A plugin for assisting in building sbt projects using GitHub Actions, in the style of sbt-travisci. Unlike sbt-travisci,
though, this plugin also provides a mechanism for generating GitHub Actions workflows from the sbt build definition.
Conceptually, sbt-travisci allows Travis and sbt to jointly represent the "source of truth", while sbt-github-actions
idiomatically vests that power solely in sbt.

Note that the generative functionality is optional and doesn't need to be used if undesired.

An example of how this "source of truth" pattern differs between the two plugins can be seen with crossScalaVersions.
With sbt-travisci, the crossScalaVersions and scalaVersion settings are populated from the scala:
key in .travis.yml. However, with sbt-github-actions, the scala: entry in the job matrix: is populated from the
ThisBuild / crossScalaVersions key in your build.sbt.

### [sbt-native-packager](https://github.com/sbt/sbt-native-packager)

SBT native packager lets you build application packages in native formats. It offers different archetypes for common
configurations, such as simple Java apps or server applications.

In your build.sbt enable the plugin you want. For example the JavaAppPackaging.

```scala
enablePlugins(JavaAppPackaging)
```

Or if you need a server with autostart support

```scala
enablePlugins(JavaServerAppPackaging)
```

### [sbt-tpolecat](https://github.com/DavidGregory084/sbt-tpolecat)

sbt-tpolecat is an SBT plugin for automagically configuring scalac options according to the project Scala version,
inspired by Rob Norris (@tpolecat)'s excellent series of blog posts providing recommended options to get the most out of
the compiler.