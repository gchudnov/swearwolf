import sbt._

object Dependencies {

  object versions {
    val zio           = "2.0.0-RC2"
  }

  private val zio             = "dev.zio" %% "zio"               % versions.zio
  private val zioStreams      = "dev.zio" %% "zio-streams"       % versions.zio
  private val zioTest         = "dev.zio" %% "zio-test"          % versions.zio
  private val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % versions.zio
  private val zioTestSbt      = "dev.zio" %% "zio-test-sbt"      % versions.zio

  private val zioTestAll = Seq(zioTest, zioTestMagnolia, zioTestSbt)

  val Util: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
    ) ++ zioTestAll map (_ % "test")
    compile ++ test
  }

  val Shapes: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
    ) ++ zioTestAll map (_ % "test")
    compile ++ test
  }  

  val Term: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
    ) ++ zioTestAll map (_ % "test")
    compile ++ test
  }

  val Draw: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
    ) ++ zioTestAll map (_ % "test")
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
    ) ++ zioTestAll map (_ % "test")
    compile ++ test
  }

}
