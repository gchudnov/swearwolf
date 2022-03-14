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

lazy val util = (project in file("util"))
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "util",
    libraryDependencies ++= Dependencies.Util
  )

lazy val shapes = (project in file("shapes"))
  .dependsOn(util, term)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "shapes",
    libraryDependencies ++= Dependencies.Shapes
  )

lazy val term = (project in file("term"))
  .dependsOn(util)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "term",
    libraryDependencies ++= Dependencies.Term
  )

lazy val rich = (project in file("rich"))
  .dependsOn(util, term)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "rich",
    libraryDependencies ++= Dependencies.Rich
  )

lazy val utilZio = (project in file("zio/util"))
  .dependsOn(util)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "util-zio",
    libraryDependencies ++= Dependencies.Zio
  )

lazy val termZio = (project in file("zio/term"))
  .dependsOn(utilZio, term)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "term-zio",
    libraryDependencies ++= Dependencies.Zio
  )

lazy val richZio = (project in file("zio/rich"))
  .dependsOn(utilZio, termZio, rich)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "rich-zio",
    libraryDependencies ++= Dependencies.Zio
  )  

lazy val shapesZio = (project in file("zio/shapes"))
  .dependsOn(utilZio, termZio, shapes)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.sonatype)
  .settings(
    name := "rich-shapes",
    libraryDependencies ++= Dependencies.Zio
  )  

lazy val exampleEither = (project in file("examples/either"))
  .dependsOn(util, term, rich, shapes)
  .settings(allSettings: _*)
  .settings(Settings.assemblySettings)
  .settings(Settings.noPublish)
  .settings(
    name := "example-either",
    libraryDependencies ++= Dependencies.ExampleAll,
    assembly / mainClass       := Some("com.github.gchudnov.swearwolf.example.either.Main"),
    assembly / assemblyOption  := (assembly / assemblyOption).value.withPrependShellScript(prependShellScript = Some(defaultUniversalScript(shebang = true))),
    assembly / assemblyJarName := s"${name.value}"
  )

lazy val exampleZio = (project in file("examples/zio"))
  .dependsOn(termZio, richZio, shapesZio)
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
  .aggregate(util, term, shapes, rich, utilZio, termZio, richZio, shapesZio, exampleEither, exampleZio)
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
