package com.xebia.kotlin.innovation

import kotlinx.coroutines.runBlocking

fun main() {
  println(eitherExample("blaa!"))
  println(eitherExample("3"))

  println(safeHeadCall())
  println(regularListOperations())


  runBlocking {
    println(readFile(null).toEither())
    println(readFile("").toEither())
    println(readFile("app/src/main/kotlin/com/xebia/kotlin/innovation/App.kt").toEither())
  }
}
