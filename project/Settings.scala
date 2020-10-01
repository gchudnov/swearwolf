import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.MergeStrategy

object Settings {
  private val scalaV = "2.13.3"

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

  val assemblySettings: Seq[Setting[_]] = Seq(
    test in assembly := {},
    assemblyOutputPath in assembly := new File("./target") / (assemblyJarName in assembly).value,
    assemblyMergeStrategy in assembly := defaultMergeStrategy((assemblyMergeStrategy in assembly).value)
  )

  val sharedResolvers: Vector[MavenRepository] = Seq(
    Resolver.jcenterRepo,
    Resolver.mavenLocal
  ).toVector

  val shared: Seq[Setting[_]] = Seq(
    scalacOptions ++= sharedScalacOptions,
    scalaVersion := scalaV,
    ThisBuild / turbo := true,
    resolvers := Resolver.combineDefaultResolvers(sharedResolvers),
    compileOrder := CompileOrder.JavaThenScala,
    organization := "com.github.gchudnov"
  )

  val testZioSettings: Seq[Setting[_]] = Seq(
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
}
