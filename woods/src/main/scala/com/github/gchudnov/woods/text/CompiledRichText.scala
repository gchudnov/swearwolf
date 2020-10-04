package com.github.gchudnov.woods.text

import com.github.gchudnov.woods.RichText

private[woods] final case class CompiledRichText(bytes: Array[Byte]) extends RichText
