package com.github.gchudnov.swearwolf.example

import com.github.gchudnov.swearwolf.util.{Color, NamedColor}

object ListMain extends App {

  // before running, make `NamedColor.colors` public
  val names = NamedColor.colors.keys.toList.sorted

  val items = names.map(name => {
    val c = NamedColor.colors(name)
    val hex = Color.toHex(c).toUpperCase()
    List(s" ![${hex}](https://placehold.it/15/${hex.substring(1)}/000000?text=+) ", s" ${name} ", s" ${c} ", s" ${hex} ")
  })

  val header = List(
    List(" Color ", " Name ", " Code ", " Hex "),
    List(" --- ", " --- ", " --- ", " --- ")
  )

  (header ++ items).foreach(is => {
    println(is.mkString("|", "|", "|"))
  })

}
