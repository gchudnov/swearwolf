import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.defaultUniversalScript

Global / cancelable := true

def testFilter(name: String): Boolean = (name endsWith "Spec")

lazy val testSettings = Seq(
  testOptions in Test ++= Seq(Tests.Filter(testFilter))
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

lazy val example = (project in file("example"))
  .dependsOn(core, woods)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.assemblySettings)
  .settings(Settings.noPublish)
  .settings(
    name := "example",
    libraryDependencies ++= Dependencies.Example,
    mainClass in assembly := Some("com.github.gchudnov.swearwolf.example.Main"),
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = true))),
    assemblyJarName in assembly := s"${name.value}"
  )

lazy val root = (project in file("."))
  .aggregate(core, woods)
  .settings(allSettings: _*)
  .settings(Settings.noPublish)
  .settings(
    name := "swearwolf"
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
