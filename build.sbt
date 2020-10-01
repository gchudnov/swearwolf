import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.defaultUniversalScript

autoStartServer := false
Global / cancelable := true

def testFilter(name: String): Boolean = (name endsWith "Spec")

lazy val testSettings = Seq(
  testOptions in Test ++= Seq(Tests.Filter(testFilter))
)

lazy val allSettings = Settings.shared ++ testSettings

lazy val swearwolf = (project in file("lib"))
  .settings(allSettings: _*)
  .settings(
    name := "swearwolf",
    libraryDependencies ++= Dependencies.Swearwolf
  )

lazy val woods = (project in file("woods"))
  .dependsOn(swearwolf)
  .settings(allSettings: _*)
  .settings(
    name := "woods",
    libraryDependencies ++= Dependencies.Woods
  )

lazy val example = (project in file("example"))
  .dependsOn(swearwolf, woods)
  .settings(allSettings: _*)
  .settings(Settings.testZioSettings)
  .settings(Settings.assemblySettings)
  .settings(
    name := "example",
    libraryDependencies ++= Dependencies.Example,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.github.gchudnov.swearexample",
    mainClass in assembly := Some("com.github.gchudnov.swearexample.Example1"),
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = true))),
    assemblyJarName in assembly := s"${name.value}"
  )

lazy val root = (project in file("."))
  .aggregate(swearwolf, woods)
  .settings(allSettings: _*)
  .settings(
    name := "swearwolf-woods"
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
