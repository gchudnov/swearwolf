package com.github.gchudnov.swearwolf.woods

import com.github.gchudnov.swearwolf.woods.box.BoxSyntax
import com.github.gchudnov.swearwolf.woods.graph.GraphSyntax
import com.github.gchudnov.swearwolf.woods.grid.GridSyntax
import com.github.gchudnov.swearwolf.woods.label.LabelSyntax
import com.github.gchudnov.swearwolf.woods.table.TableSyntax
import com.github.gchudnov.swearwolf.woods.text.RichTextSyntax

private[woods] trait AllSyntax extends BoxSyntax with GraphSyntax with GridSyntax with LabelSyntax with TableSyntax with RichTextSyntax

object AllSyntax extends AllSyntax
