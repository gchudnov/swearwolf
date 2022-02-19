package com.github.gchudnov.swearwolf.woods.box

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.swearwolf.woods.Box
import com.github.gchudnov.swearwolf.woods.box.impl.BoxDrawer

private[box] class BoxOps(private val screen: Screen) extends AnyVal:
  def put(pt: Point, box: Box, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
    BoxDrawer.draw(screen)(pt, box, textStyle)

private[woods] trait BoxSyntax:
  implicit def boxOps(screen: Screen): BoxOps = new BoxOps(screen)

object BoxSyntax extends BoxSyntax
