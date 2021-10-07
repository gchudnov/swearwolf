import com.jsuereth.sbtpgp.PgpKeys
import com.jsuereth.sbtpgp.SbtPgp.autoImport.usePgpKeyHex
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.MergeStrategy
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._

object Settings {
  private val scala213 = "2.13.6"
  private val scalaV   = scala213

  private val sharedScalacOptions = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8",                         // Specify character encoding used by source files.
    "-explaintypes",                 // Explain type errors in more detail.
    "-feature",                      // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds",         // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-language:postfixOps",          // Enable postfixOps
    "-unchecked",                    // Enable additional warnings where generated code depends on assumptions.
    "-Xlint",
    "-Ywarn-numeric-widen" // Warn when numerics are widened.
  )

  type MergeStrategySelector = String => MergeStrategy

  def defaultMergeStrategy(fallbackStrategy: MergeStrategySelector): MergeStrategySelector = {
    case x if x.contains("module-info.class")            => MergeStrategy.discard
    case x if x.contains("io.netty.versions.properties") => MergeStrategy.filterDistinctLines
    case x                                               => fallbackStrategy(x)
  }

  val supportedScalaVersions = List(scala213)

  val assemblySettings: Seq[Setting[_]] = Seq(
    assembly / test                  := {},
    assembly / assemblyOutputPath    := new File("./target") / (assembly / assemblyJarName).value,
    assembly / assemblyMergeStrategy := defaultMergeStrategy((assembly / assemblyMergeStrategy).value)
  )

  val sharedResolvers: Vector[MavenRepository] = Seq(
    Resolver.jcenterRepo,
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases")
  ).toVector

  val shared: Seq[Setting[_]] = Seq(
    scalacOptions      := sharedScalacOptions,
    crossScalaVersions := supportedScalaVersions,
    scalaVersion       := scalaV,
    ThisBuild / turbo  := true,
    resolvers          := Resolver.combineDefaultResolvers(sharedResolvers),
    compileOrder       := CompileOrder.JavaThenScala,
    organization       := "com.github.gchudnov",
    homepage           := Some(url("https://github.com/gchudnov/swearwolf")),
    description        := "A low level Scala library for creating text user interfaces.",
    licenses           := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/gchudnov/swearwolf"),
        "scm:git@github.com:gchudnov/swearwolf.git"
      )
    ),
    developers := List(
      Developer(id = "gchudnov", name = "Grigorii Chudnov", email = "g.chudnov@gmail.com", url = url("https://github.com/gchudnov"))
    )
  )

  val noPublish: Seq[Setting[_]] = Seq(
    publishArtifact := false,
    publish         := {},
    publishLocal    := {},
    publish / skip  := true
  )

  val sonatype: Seq[Setting[_]] = Seq(
    publishMavenStyle      := true,
    Test / publishArtifact := false,
    credentials            := Seq(Credentials(Path.userHome / ".sbt" / ".credentials-sonatype")),
    usePgpKeyHex("8A64557ABEC7965C31A1DF8DE12F2C6DE96AF6D1"),
    publishTo                     := Some("Sonatype Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    releaseCrossBuild             := true,
    releaseIgnoreUntrackedFiles   := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      releaseStepCommandAndRemaining("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

  val testZioSettings: Seq[Setting[_]] = Seq(
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
}
