package com.github.gchudnov.swearwolf.example.noninteractive

import com.github.gchudnov.swearwolf.rich.instances.IdRichText.*
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.terms.IdSyncTerm
import com.github.gchudnov.swearwolf.term.writers.IdWriter
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*

/**
 * Allows to write styled text to StdOut.
 */
object Main extends App:
  import TextStyle.*

  val term   = IdSyncTerm.make()
  val writer = IdWriter.make(term)

  val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>\n")

  writer.put("HELLO ", Bold | Foreground(Color.Blue))
  writer.put("WORLD!\n", Foreground(Color.Blue) | Background(Color.Yellow))
  writer.putRich(rich)

  writer.flush()
