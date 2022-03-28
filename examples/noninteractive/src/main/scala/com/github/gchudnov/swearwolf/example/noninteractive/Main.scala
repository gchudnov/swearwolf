package com.github.gchudnov.swearwolf.example.noninteractive

import com.github.gchudnov.swearwolf.rich.IdRichText.*
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.util.*

/**
 * Allows to write styled text to StdOut.
 */
object Main extends App:
  import TextStyle.*

  val term   = Term.syncId()
  val writer = Writer.syncId(term)

  val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>\n")

  writer.put("HELLO ", Bold | Foreground(Color.Blue))
  writer.put("WORLD!\n", Foreground(Color.Blue) | Background(Color.Yellow))
  writer.putRich(rich)

  writer.flush()
