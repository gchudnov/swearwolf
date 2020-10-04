package com.github.gchudnov.woods

import com.github.gchudnov.woods.box.BoxSyntax
import com.github.gchudnov.woods.graph.GraphSyntax
import com.github.gchudnov.woods.grid.GridSyntax
import com.github.gchudnov.woods.label.LabelSyntax
import com.github.gchudnov.woods.table.TableSyntax
import com.github.gchudnov.woods.text.RichTextSyntax

private[woods] trait AllSyntax extends BoxSyntax with GraphSyntax with GridSyntax with LabelSyntax with TableSyntax with RichTextSyntax
