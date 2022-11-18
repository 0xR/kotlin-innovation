package com.xebia.kotlin.innovation

import arrow.core.Either
import arrow.core.flatMap

//sampleStart
// Either Style
fun parse(s: String): Either<NumberFormatException, Int> =
  if (s.matches(Regex("-?[0-9]+"))) Either.Right(s.toInt())
  else Either.Left(NumberFormatException("$s is not a valid integer."))

fun reciprocal(i: Int): Either<IllegalArgumentException, Double> =
  if (i == 0) Either.Left(IllegalArgumentException("Cannot take reciprocal of 0."))
  else Either.Right(1.0 / i)

fun stringify(d: Double): Either<Nothing, String> = Either.Right(d.toString())
fun eitherExample(s: String): Either<IllegalArgumentException, String> =
  parse(s).flatMap(::reciprocal).flatMap(::stringify)
