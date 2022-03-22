package com.github.gchudnov.swearwolf.util.func

final case class Canceler(cancel: () => Unit)
