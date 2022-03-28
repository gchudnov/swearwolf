package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.instances.AllColor

package object util extends AllColor:

  export bytes.Bytes
  export colors.Color
  export func.{ Canceler, EitherMonad, FutureMonad, IdMonad, Identity, MonadError, OptionMonad, TryMonad }
  export geometry.{ Point, Rect, Size }
  export layout.Layout
  export logging.FileLogging
  export spans.Span
  export styles.{ AlignStyle, TextStyle }
