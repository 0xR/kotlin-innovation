package com.xebia.kotlin.innovation

import arrow.core.getOrElse
import kotlinx.coroutines.runBlocking

fun main() {
  println(eitherExample("blaa!"))
  println(eitherExample("3"))

  println("\nNon Empty List Example")
  safeHeadCall()
  regularListOperations()

  println("\nOption Example")
  println(getOptionalResponse().getOrElse { "Sad you didn't get a response, the server doesn't love you" })
  println(optionalFromNullable())
  mappingOptions()


  println("\nEffects Example")
  runBlocking {
    println(readFile(null).toEither())
    println(readFile("").toEither())
    println(readFile("app/src/main/kotlin/com/xebia/kotlin/innovation/App.kt").toEither())
  }
}
