package com.github.gchudnov.swearwolf.util.data

sealed trait Stack[+T]:
  def push[U >: T](t: U): Stack[U]
  def pop(): (T, Stack[T])
  def top: T
  def size: Int
  def isEmpty: Boolean
  def nonEmpty: Boolean

object Stack:
  def empty[T]: Stack[T] =
    EmptyStack

sealed abstract class AStack[+T] extends Stack[T]:
  def push[U >: T](t: U): AStack[U] = new NonEmptyStack(t, this)

case object EmptyStack extends AStack[Nothing]:
  def pop(): (Nothing, Stack[Nothing]) = throw new NoSuchElementException("pop of empty stack")
  def top: Nothing                     = throw new NoSuchElementException("top of empty stack")
  def size: Int                        = 0
  def isEmpty: Boolean                 = true
  def nonEmpty: Boolean                = !isEmpty

final case class NonEmptyStack[+T](top: T, rest: Stack[T]) extends AStack[T]:
  def pop(): (T, Stack[T]) = (top, rest)
  def size: Int            = 1 + rest.size
  def isEmpty: Boolean     = false
  def nonEmpty: Boolean    = !isEmpty

object NonEmptyStack:
  def apply[T](t: T): NonEmptyStack[T] = new NonEmptyStack(t, EmptyStack)
