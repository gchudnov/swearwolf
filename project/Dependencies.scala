import sbt._

object Dependencies {
  object versions {
    val scalaXml     = "2.0.1"
    val zio           = "2.0.0-RC2"
  }
  private val scalaXml = "org.scala-lang.modules" %% "scala-xml" % versions.scalaXml

  private val zio             = "dev.zio" %% "zio"               % versions.zio
  private val zioStreams      = "dev.zio" %% "zio-streams"       % versions.zio
  private val zioTest         = "dev.zio" %% "zio-test"          % versions.zio
  private val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % versions.zio
  private val zioTestSbt      = "dev.zio" %% "zio-test-sbt"      % versions.zio

  val Term: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
      zioTest,
      zioTestMagnolia,
      zioTestSbt
    ) map (_ % "test")
    compile ++ test
  }

  val Woods: Seq[ModuleID] = {
    val compile = Seq(
      scalaXml
    )
    val test = Seq(
      zioTest,
      zioTestMagnolia,
      zioTestSbt
    ) map (_ % "test")
    compile ++ test
  }

  val ExamplePlain: Seq[ModuleID] = {
    val compile = Seq.empty[ModuleID]
    val test    = Seq.empty[ModuleID] map (_ % "test")
    compile ++ test
  }

  val ExampleZio: Seq[ModuleID] = {
    val compile = Seq(
      zio,
      zioStreams
    )
    val test = Seq(
      zioTest,
      zioTestMagnolia,
      zioTestSbt
    ) map (_ % "test")
    compile ++ test
  }

}
