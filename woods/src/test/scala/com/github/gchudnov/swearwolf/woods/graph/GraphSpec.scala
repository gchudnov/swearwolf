package com.github.gchudnov.swearwolf.woods.graph

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.woods.GraphStyle
import com.github.gchudnov.swearwolf.woods.graph.impl.GraphDrawer
import zio.test.Assertion.*
import zio.test.*

object GraphSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Graph")(
      test("uStep") {
        val dy = 25.0    // height of 1 cell
        val sy = 4       // number of y-steps
        val cy = dy / sy // 6.25 per y-step ; 3.125 = half of the step

        // [0     - 6.25)
        // [6.25  - 12.5)
        // [12.5  - 18.75)
        // [18.75 - 25)
        val table = List(
          (0.0   -> 0),
          (1.0   -> 0),
          (3.1   -> 0),
          (5.0   -> 0),
          (6.25  -> 1),
          (8.0   -> 1),
          (9.3   -> 1),
          (10.0  -> 1),
          (12.0  -> 1),
          (12.5  -> 2),
          (14.0  -> 2),
          (16.0  -> 2),
          (18.0  -> 2),
          (18.75 -> 3),
          (20.0  -> 3),
          (22.0  -> 3),
          (24.0  -> 3),
          (25.0  -> 4)
        )

        val input    = table.map(_._1)
        val expected = table.map(_._2)

        val actual = input.map(it => GraphDrawer.uStep(cy)(it))

        assert(actual)(equalTo(expected))
      },
      test("yStep") {
        val dy = 25.0    // height of 1 cell
        val sy = 4       // number of y-steps
        val cy = dy / sy // 6.25 per y-step ; 3.125 = half of the step

        val y0 = 0.0
        val y1 = 25.0

        // [0     - 6.25)
        // [6.25  - 12.5)
        // [12.5  - 18.75)
        // [18.75 - 25)
        val table = List(
          (-10.0 -> 0),
          (0.0   -> 0),
          (5.0   -> 1),
          (10.0  -> 2),
          (16.0  -> 3),
          (22.0  -> 4),
          (25.0  -> 4),
          (26.0  -> 4),
          (50.0  -> 4)
        )

        val input    = table.map(_._1)
        val expected = table.map(_._2)

        val actual = input.map(it => GraphDrawer.yStep(sy, cy)(y0, y1)(it))

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line") {
        val sz    = Size(8, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(0.0, 24.0, 25.0, 50.0, 74.0, 75.0, 100.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("     ⣠⣶⣿")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with values scaling") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(0.0 * 2, 24.0 * 2, 25.0 * 2, 50.0 * 2, 74.0 * 2, 75.0 * 2, 100.0 * 2)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq(" ⣠⣶⣿")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile two line") {
        val sz    = Size(4, 2)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(0.0, 24.0, 25.0, 50.0, 74.0, 75.0, 100.0)

        val actual = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq(
          "  ⢠⣾",
          " ⣴⣿⣿"
        )

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 0, with max") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option(100.0)

        val input = List(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("    ")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 0, no max") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("    ")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 24.0, no max") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(24.0, 24.0, 24.0, 24.0, 24.0, 24.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq(" ⣿⣿⣿")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 24.0, with max") {
        val sz    = Size(3, 1)
        val ceilY = 100.0
        val maxY  = Option(ceilY)

        val input = List(24.0, 24.0, 24.0, 24.0, 24.0, 24.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("⣀⣀⣀")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 49.0, no max") {
        val sz    = Size(3, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(49.0, 49.0, 49.0, 49.0, 49.0, 49.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("⣿⣿⣿")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line with value 49.0, with max") {
        val sz    = Size(3, 1)
        val ceilY = 100.0
        val maxY  = Option(ceilY)

        val input = List(49.0, 49.0, 49.0, 49.0, 49.0, 49.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("⣤⣤⣤")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line up and down, no max") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        val input = List(0.0, 24.0, 25.0, 50.0, 75.0, 50.0, 25.0, 10.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("⢠⣴⣷⣄")

        assert(actual)(equalTo(expected))
      },
      test("dot - compile one line up and down, with max") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option(ceilY)

        val input = List(0.0, 24.0, 25.0, 50.0, 75.0, 50.0, 25.0, 10.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Dot)
        val expected = Seq("⢀⣴⣷⣄")

        assert(actual)(equalTo(expected))
      },
      test("step - compile one line up") {
        val sz    = Size(10, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Step)
        val expected = Seq(" ▁▂▃▄▅▆▇██")

        assert(actual)(equalTo(expected))
      },
      test("step - compile one line up and down") {
        val sz    = Size(18, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0, 87.5, 75.0, 62.5, 50.0, 37.5, 25.0, 12.5, 0.1)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Step)
        val expected = Seq(" ▁▂▃▄▅▆▇███▇▆▅▄▃▂▁")

        assert(actual)(equalTo(expected))
      },
      test("step - fit in size with size is smaller than data") {
        val sz    = Size(4, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Step)
        val expected = Seq("▆▇██")

        assert(actual)(equalTo(expected))
      },
      test("step - fit in size with size is bigger than data") {
        val sz    = Size(10, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Step)
        val expected = Seq(" ▁▂▃▄▅▆▇██")

        assert(actual)(equalTo(expected))
      },
      test("quad - compile one line up") {
        val sz    = Size(5, 1)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0)

        val actual   = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Quad)
        val expected = Seq("▗▄▟██")

        assert(actual)(equalTo(expected))
      },
      test("quad - compile two line up") {
        val sz    = Size(5, 2)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0)

        val actual = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Quad)
        val expected = Seq(
          "   ▟█",
          "▗▟███"
        )

        assert(actual)(equalTo(expected))
      },
      test("quad - empty input") {
        val sz    = Size(5, 2)
        val ceilY = 100.0
        val maxY  = Option.empty[Double]

        // step = 12.5
        val input = List.empty[Double]

        val actual = GraphDrawer.compile(sz, ceilY, maxY)(input, GraphStyle.Quad)
        val expected = Seq(
          "     ",
          "     "
        )

        assert(actual)(equalTo(expected))
      }
    )
