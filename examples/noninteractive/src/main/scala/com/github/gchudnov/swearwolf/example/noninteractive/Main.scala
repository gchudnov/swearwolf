package com.github.gchudnov.swearwolf.example.noninteractive

import com.github.gchudnov.swearwolf.rich.instances.EitherRichText.*
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.terms.EitherSyncTerm
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.term.writers.EitherWriter
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*

/**
 * Allows to write styled text to StdOut.
 */
object Main extends App:
  import TextStyle.*

  val term   = EitherSyncTerm.make()
  val writer = EitherWriter.make(term)

  val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>\n")

  for
    _ <- writer.put("HELLO ", Bold | Foreground(Color.Blue))
    _ <- writer.put("WORLD!\n", Foreground(Color.Blue) | Background(Color.Yellow))
    _ <- writer.putRich(rich)
    _ <- writer.flush()
  yield ()
