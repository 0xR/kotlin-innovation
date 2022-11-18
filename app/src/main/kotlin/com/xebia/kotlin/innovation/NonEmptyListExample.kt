package com.xebia.kotlin.innovation

import arrow.core.nonEmptyListOf
import arrow.core.toNonEmptyListOrNull
import kotlin.random.Random

val nonEmptyList = nonEmptyListOf(1,2,3)
val nonEmptyListFromList = listOf(4, 5).toNonEmptyListOrNull()
val nullListFromEmptyList = emptyList<Int>().toNonEmptyListOrNull()

fun safeHeadCall() {
    println("Printing the result of an empty nonEmptyList ${nullListFromEmptyList?.head}")

    val regularEmptyList = emptyList<Int>()
    try {
        regularEmptyList[0]
    } catch (e: Exception) {
        // this would have been an exception instead of not
        println("Throwing an exception on a regular list get[0] instead of null")
    }

    try {
        regularEmptyList.first()
    } catch (e: Exception) {
        // this would have also
        println("Throwing an exception on a regular list first call instead of null")
    }
}

fun regularListOperations() {
    val foldResult = nonEmptyList.foldLeft(0) { total, value -> total + value}
    println("Result of fold left: $foldResult")

    val value = nonEmptyList.flatMap { one ->
        nonEmptyListFromList!!.map { two ->
            one + two
        }
    }

    println("Result from combing to lists: $value")
}