package com.xebia.kotlin.innovation

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.none
import java.util.Optional
import kotlin.random.Random

val some = Some("Hello")
val none = none<Int>()

fun getOptionalResponse(): Option<String> {
    return if (Random.nextBoolean()) Some("Hey you got a response") else None
}

fun optionalFromNullable(): Option<Option<Option<Option<Option<Option<String>>>>>> {
    val someNullableString: String? = null

    return Option.fromNullable(
        Option.fromNullable(
            Option.fromNullable(
                Option.fromNullable(
                    Option.fromNullable(
                        Option.fromNullable(
                            someNullableString
                        )
                    )
                )
            )
        )
    )
}

fun mappingOptions() {
    val mappedResult1 = some.map { "$it World" }
    val mappedResult2 = none.map { it * 1.5 }
    //sampleEnd

    println("mappedResult1 = $mappedResult1")
    println("mappedResult2 = $mappedResult2")
}