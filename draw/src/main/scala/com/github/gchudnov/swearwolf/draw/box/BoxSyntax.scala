package com.github.gchudnov.swearwolf.draw.box

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.geometry.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.draw.Box
import com.github.gchudnov.swearwolf.draw.box.impl.BoxDrawer

private[box] trait BoxOps:
  extension (screen: Screen)
    def put(pt: Point, box: Box, textStyle: TextStyle): Either[Throwable, Unit] =
      BoxDrawer.draw(screen)(pt, box, textStyle)

    def put(pt: Point, box: Box): Either[Throwable, Unit] =
      put(pt, box, TextStyle.Empty)

private[draw] trait BoxSyntax extends BoxOps

object BoxSyntax extends BoxSyntax

// TODO: change to render Seq[String] ?
