package com.xebia.kotlin.innovation

import arrow.core.Endo

fun endoExample(): String {
    // really good decorator? What is the use case..
    // I think its useful if you want to enforce manipulation that does not change type
    val helloEndo: Endo<String> = Endo {
        "hello"
    }

    val worldEndo: Endo<String> = Endo {
        "$it world"
    }

    val endEndo = Endo<String> {
        "$it!"
    }

    return endEndo.combine(worldEndo).combine(helloEndo).f("")
}