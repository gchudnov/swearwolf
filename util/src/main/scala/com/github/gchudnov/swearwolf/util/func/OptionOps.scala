package com.github.gchudnov.swearwolf.util.func

object OptionOps:

  val optionToId: FunctionK[Option[*], Identity] =
    new FunctionK[Option[*], Identity]:
      override def apply[A](fa: Option[A]): Identity[A] =
        fa match
          case None    => throw new RuntimeException("Value is not defined")
          case Some(v) => v

  val idToOption: FunctionK[Identity, Option[*]] =
    new FunctionK[Identity, Option[*]]:
      override def apply[A](fa: Identity[A]): Option[A] = Some(fa)
