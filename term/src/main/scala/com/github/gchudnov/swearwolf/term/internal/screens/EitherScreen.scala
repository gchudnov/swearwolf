package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.SyncScreen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.term.internal.screens.ShellScreen
import com.github.gchudnov.swearwolf.term.internal.screens.TermAction
import com.github.gchudnov.swearwolf.util.geometry.Size

final class EitherScreen(term: Term[Either[Throwable, *]], cleanup: TermAction[Either[Throwable, *]], sz: Option[Size]) extends SyncScreen(term):

  override def close(): Either[Throwable, Unit] =
    cleanup(term)
      .flatMap(_ => term.close())

object EitherScreen {
  // def apply(term: Term[Either[Throwable, *]], rollback: List[TermAction[Either[Throwable, *]]]): EitherShellScreen =
  //   new EitherShellScreen(term, rollback)
}

// final class EitherEventLoop(term: Term[Either[Throwable, *]]) extends SyncEventLoop[Either[Throwable, *]](term)(EitherMonad):

// abstract class SyncScreen[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyScreen[F](term)(ME) {}

/*


  override def close(): Unit =
    val err = Transform.sequence(rollback.map(_.apply(term)))
    err.toTry.get

private[term] object TermScreen:

  /**
 * Makes a new TermScreen.
 */
  def make(term: Term): Either[Throwable, TermScreen] =
    val (errOrOk, rollback) = initialize(term)
    errOrOk match
      case Left(t) =>
        // NOTE: we roll-back and ignore the errors to avoid error in the error
        rollback.foreach(_(term))
        Left(t)
      case Right(_) =>
        val screen = new TermScreen(term, rollback)
        Right(screen)


  private def initialize(term: Term): (Either[Throwable, Unit], List[TermAction]) =
    initEffects.foldLeft((Right(()): Either[Throwable, Unit], List.empty[TermAction])) { case ((err, acc), eff) =>
      err match
        case Left(t) => (Left(t), acc)
        case Right(_) =>
          val (effect, rollback) = eff
          effect(term) match
            case Left(t)  => (Left(t), acc)
            case Right(_) => (Right(()), rollback :: acc)
    }

 */