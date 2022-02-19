import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.defaultUniversalScript

Global / cancelable := true
Global / scalaVersion := Settings.globalScalaVersion
Global / semanticdbEnabled := true

def testFilter(name: String): Boolean = (name endsWith "Spec")

lazy val testSettings = Seq(
  Test / testOptions ++= Seq(Tests.Filter(testFilter))
)

lazy val allSettings = Settings.shared ++ testSettings

lazy val core = (project in file("core"))
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "swearwolf-core",
    libraryDependencies ++= Dependencies.Swearwolf
  )

lazy val woods = (project in file("woods"))
  .dependsOn(core)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "swearwolf-woods",
    libraryDependencies ++= Dependencies.Woods
  )

lazy val examplePlain = (project in file("examples/plain"))
  .dependsOn(core, woods)
  .settings(allSettings: _*)
  .settings(Settings.assemblySettings)
  .settings(Settings.noPublish)
  .settings(
    name := "example-plain",
    libraryDependencies ++= Dependencies.ExamplePlain,
    assembly / mainClass       := Some("com.github.gchudnov.swearwolf.example.plain.Main"),
    assembly / assemblyOption  := (assembly / assemblyOption).value.withPrependShellScript(prependShellScript = Some(defaultUniversalScript(shebang = true))),
    assembly / assemblyJarName := s"${name.value}"
  )

lazy val exampleZio = (project in file("examples/zio"))
  .dependsOn(core, woods)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.assemblySettings)
  .settings(Settings.noPublish)
  .settings(
    name := "example-zio",
    libraryDependencies ++= Dependencies.ExampleZio,
    assembly / mainClass       := Some("com.github.gchudnov.swearwolf.example.zio.Main"),
    assembly / assemblyOption  := (assembly / assemblyOption).value.withPrependShellScript(prependShellScript = Some(defaultUniversalScript(shebang = true))),
    assembly / assemblyJarName := s"${name.value}"
  )

lazy val root = (project in file("."))
  .aggregate(core, woods, examplePlain, exampleZio)
  .settings(allSettings: _*)
  .settings(Settings.noPublish)
  .settings(
    name := "swearwolf"
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
addCommandAlias("plg", "; reload plugins ; libraryDependencies ; reload return")
// NOTE: to use version check for plugins, add to the meta-project (/project/project) sbt-updates.sbt with "sbt-updates" plugin as well.
addCommandAlias("upd", ";dependencyUpdates; reload plugins; dependencyUpdates; reload return")
