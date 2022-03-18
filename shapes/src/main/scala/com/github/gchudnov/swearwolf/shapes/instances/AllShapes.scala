package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.util.func.MonadError

trait AllShapes[F[_]: MonadError] extends AnyBox[F] with AnyChart[F] with AnyGrid[F] with AnyLabel[F] with AnyTable[F]
