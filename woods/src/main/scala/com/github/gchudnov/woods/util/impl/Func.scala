package com.github.gchudnov.woods.util.impl

private[woods] object Func {

  def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match {
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)
    }

}
