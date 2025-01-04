import com.jsuereth.sbtpgp.PgpKeys
import com.jsuereth.sbtpgp.SbtPgp.autoImport.usePgpKeyHex
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.MergeStrategy
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._

object Settings {
  private val scala3 = "3.3.4"
  private val scalaV = scala3

  private val sharedScalacOptions = Seq(
    "-deprecation", // emit warning and location for usages of deprecated APIs
//    "-explain",                      // explain errors in more detail
//    "-explain-types",                // explain type errors in more detail
    "-feature",                      // emit warning and location for usages of features that should be imported explicitly
    "-indent",                       // allow significant indentation.
    "-new-syntax",                   // require scala 3.0 new syntax.
    "-print-lines",                  // show source code line numbers.
    "-unchecked",                    // enable additional warnings where generated code depends on assumptions
    "-Ykind-projector",              // allow `*` as wildcard to be compatible with kind projector
    "-Xfatal-warnings",              // fail the compilation if there are any warnings
    "-Xmigration",                   // warn about constructs whose behavior may have changed since version
    "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds",         // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-language:postfixOps",          // Enable postfixOps
  )

  type MergeStrategySelector = String => MergeStrategy

  def defaultMergeStrategy(fallbackStrategy: MergeStrategySelector): MergeStrategySelector = {
    case x if x.contains("module-info.class")            => MergeStrategy.discard
    case x if x.contains("io.netty.versions.properties") => MergeStrategy.filterDistinctLines
    case x                                               => fallbackStrategy(x)
  }

  val globalScalaVersion: String           = scalaV
  val supportedScalaVersions: List[String] = List(scala3)

  val assemblySettings: Seq[Setting[?]] = Seq(
    assembly / test                  := {},
    assembly / assemblyOutputPath    := new File("./target") / (assembly / assemblyJarName).value,
    assembly / assemblyMergeStrategy := defaultMergeStrategy((assembly / assemblyMergeStrategy).value),
  )

  val sharedResolvers: Vector[MavenRepository] = (Seq(Resolver.mavenLocal) ++ Resolver.sonatypeOssRepos("releases")).toVector

  val shared: Seq[Setting[?]] = Seq(
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
        "scm:git@github.com:gchudnov/swearwolf.git",
      )
    ),
    developers := List(
      Developer(id = "gchudnov", name = "Grigorii Chudnov", email = "g.chudnov@gmail.com", url = url("https://github.com/gchudnov"))
    ),
  )

  val noPublish: Seq[Setting[?]] = Seq(
    publishArtifact := false,
    publish         := {},
    publishLocal    := {},
    publish / skip  := true,
  )

  val sonatype: Seq[Setting[?]] = Seq(
    publishMavenStyle      := true,
    Test / publishArtifact := false,
    credentials            := Seq(Credentials(Path.userHome / ".sbt" / ".credentials-sonatype")),
    usePgpKeyHex("8A64557ABEC7965C31A1DF8DE12F2C6DE96AF6D1"),
    publishTo                     := Some("Sonatype Releases".at("https://oss.sonatype.org/service/local/staging/deploy/maven2")),
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
      pushChanges,
    ),
  )

  val testZioSettings: Seq[Setting[?]] = Seq(
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
}
