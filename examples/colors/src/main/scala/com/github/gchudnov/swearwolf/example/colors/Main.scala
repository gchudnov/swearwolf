package com.github.gchudnov.swearwolf.example.colors

import com.github.gchudnov.swearwolf.util.colors.Color

import java.nio.file.{ Path, Paths }
import scala.sys.process.*

/**
 * Generates PNG-images for the predefined colors and produces a table that is used in `util/COLORS.md`.
 *
 * NOTE: Depends on `convert` utility to generate images.
 */
object Main extends App:

  private val classPath: String = getClass.getResource("").getPath
  private val rootPath: String  = classPath.substring(0, classPath.indexOf("examples"))
  private val utilDir: String   = "util"
  private val relPath: String   = "res/colors"
  private val outPath: Path     = Paths.get(rootPath, utilDir, relPath)

  private val colorNames: List[String] = Color.namedColors.keys.toList.sorted

  private val szWidth: Int  = 32
  private val szHeight: Int = 16

  val rows = colorNames.map(name =>
    val color     = Color.namedColors(name)
    val hex       = color.toHex.toUpperCase()
    val imageName = s"${name}.png"

    val imageRelPath  = Paths.get(relPath, imageName)
    val imageFilePath = outPath.resolve(imageName)

    val execCmd = s"convert -size ${szWidth}x${szHeight} xc:${hex} ${imageFilePath}"
    execCmd !

    val colorTypeName = s"Color.${toTypeName(name)}"

    List(
      s"![${hex}](${imageRelPath})",
      s"${name}",
      s"${colorTypeName}",
      s"${color}",
      s"${hex}"
    ).map(wrap)
  )

  val header = List(
    List("View", "Name", "Color", "Code", "Hex").map(wrap),
    List("---", "---", "---", "---", "---").map(wrap)
  )

  val lines = header ++ rows

  lines.foreach(it => println(it.mkString("|", "|", "|")))

  private def toTypeName(name: String): String =
    val withTail = Range('a', 'z').foldLeft(name)((acc, ch) => acc.replace(s"-${ch.toChar}", s"${ch.toChar.toString.toUpperCase()}"))
    val withHead = withTail.substring(0, 1).toUpperCase + withTail.substring(1)
    withHead

  private def wrap(s: String): String =
    s" ${s} "
