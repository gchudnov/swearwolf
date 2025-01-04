package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.AnyTerm
import com.github.gchudnov.swearwolf.term.terms.IdSyncTerm.make
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.io.{ BufferedOutputStream, InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadError[F])
    extends AnyTerm[F](in, out, isClose) {}
