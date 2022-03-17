package com.github.gchudnov.swearwolf.rich.instances

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.{ Screen, Writer }
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point

trait AnyRichText:

  extension [F[_]](writer: Writer[F])
    def put(richText: RichText)(using MF: MonadError[F]): F[Unit] =
      RichText.put(writer, richText)

  extension [F[_]](screen: Screen[F])
    def put(pt: Point, richText: RichText)(using MF: MonadError[F]): F[Unit] =
      RichText.put(screen, pt, richText)
