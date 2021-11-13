import sbt._

object Dependencies {
  object versions {
    val fastparse     = "2.3.3"
    val kindProjector = "0.10.3"
    val zio           = "2.0.0-M5"
  }

  private val kindProjector = compilerPlugin(
    "org.typelevel" %% "kind-projector" % versions.kindProjector
  )

  private val compiler = Seq(
    kindProjector
  )

  private val fastparse = "com.lihaoyi" %% "fastparse" % versions.fastparse

  private val zio             = "dev.zio" %% "zio"               % versions.zio
  private val zioStreams      = "dev.zio" %% "zio-streams"       % versions.zio
  private val zioTest         = "dev.zio" %% "zio-test"          % versions.zio
  private val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % versions.zio
  private val zioTestSbt      = "dev.zio" %% "zio-test-sbt"      % versions.zio

  val Woods: Seq[ModuleID] = {
    val compile = Seq(
      fastparse
    )
    val test = Seq(
      zioTest,
      zioTestMagnolia,
      zioTestSbt
    ) map (_ % "test")
    compile ++ test ++ compiler
  }

  val Swearwolf: Seq[ModuleID] = {
    val compile = Seq(
    )
    val test = Seq(
      zioTest,
      zioTestMagnolia,
      zioTestSbt
    ) map (_ % "test")
    compile ++ test ++ compiler
  }

  val ExamplePlain: Seq[ModuleID] = {
    val compile = Seq.empty[ModuleID]
    val test = Seq.empty[ModuleID] map (_ % "test")
    compile ++ test ++ compiler
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
    compile ++ test ++ compiler
  }

}
