package com.github.gchudnov.woods.text

import com.github.gchudnov.swearwolf.Screen

trait RichTextSyntax {
  implicit def richTextOps(screen: Screen): RichTextOps = new RichTextOps(screen)
}
