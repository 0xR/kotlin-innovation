package com.xebia.kotlin.innovation

import arrow.core.andThen

//sampleStart
val throwsSomeStuff: (Int) -> Double = { x -> x.toDouble() }
val throwsOtherThings: (Double) -> String = { x -> x.toString() }
val moreThrowing: (String) -> List<String> = { x -> listOf(x) }
val magic = throwsSomeStuff.andThen(throwsOtherThings).andThen(moreThrowing)

//sampleEnd
fun main() {
  println("magic = $magic")
}
