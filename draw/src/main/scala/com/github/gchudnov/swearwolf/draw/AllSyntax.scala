package com.github.gchudnov.swearwolf.draw

import com.github.gchudnov.swearwolf.draw.box.BoxSyntax
import com.github.gchudnov.swearwolf.draw.graph.GraphSyntax
import com.github.gchudnov.swearwolf.draw.grid.GridSyntax
import com.github.gchudnov.swearwolf.draw.label.LabelSyntax
import com.github.gchudnov.swearwolf.draw.table.TableSyntax
import com.github.gchudnov.swearwolf.draw.text.RichTextSyntax

private[draw] trait AllSyntax extends BoxSyntax with GraphSyntax with GridSyntax with LabelSyntax with TableSyntax with RichTextSyntax

object AllSyntax extends AllSyntax
