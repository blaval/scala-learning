addSbtPlugin("com.github.sbt"   % "sbt-native-packager"  % "1.9.9")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
// automagically configuring scalac options according to the project Scala version https://github.com/DavidGregory084/sbt-tpolecat
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")
// assisting in building sbt projects using GitHub Actions https://github.com/djspiewak/sbt-github-actions
addSbtPlugin("com.codecommit" % "sbt-github-actions" % "0.14.2")
