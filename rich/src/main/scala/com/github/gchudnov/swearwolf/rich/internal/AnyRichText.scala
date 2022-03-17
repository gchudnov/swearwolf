package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.{ Screen, Writer }
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point

trait AnyRichText[F[_]: MonadError]:

  extension (writer: Writer[F])
    def putRich(richText: RichText): F[Unit] =
      RichText.put(writer, richText)

  extension (screen: Screen[F])
    def putRich(pt: Point, richText: RichText): F[Unit] =
      RichText.put(screen, pt, richText)
