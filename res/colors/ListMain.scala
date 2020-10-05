package com.github.gchudnov.swearwolf.example

import com.github.gchudnov.swearwolf.util.{Color, NamedColor}

object ListMain extends App {

  // before running, make `NamedColor.colors` public
  val names = NamedColor.colors.keys.toList.sorted

  val items = names.map(name => {
    val c = NamedColor.colors(name)
    val hex = Color.toHex(c).toUpperCase()
    List(s" ![${hex}](https://placehold.it/15/${hex.substring(1)}/000000?text=+) ", s" ${name} ", s" NamedColor.${toTypeName(name)} ", s" ${c} ", s" ${hex} ")
  })

  val header = List(
    List(" Color ", " Name ", " Named ", " Code ", " Hex "),
    List(" --- ", " --- ", " --- ", " --- ", " --- ")
  )

  (header ++ items).foreach(is => {
    println(is.mkString("|", "|", "|"))
  })

  private def toTypeName(name: String): String = {
    val n1 = Range('a', 'z').foldLeft(name)((acc, ch) => {
      acc.replace(s"-${ch.toChar}", s"${ch.toChar.toString.toUpperCase()}")
    })

    val n2 = n1.substring(0, 1).toUpperCase + n1.substring(1)

    n2
  }

}
