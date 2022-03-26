package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.util.func.{ IdMonad, Identity }

sealed trait IdShapes extends AllShapes[Identity]

object IdShapes extends IdShapes
