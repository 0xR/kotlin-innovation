package com.xebia.kotlin.innovation

import arrow.core.nonEmptyListOf
import arrow.core.toNonEmptyListOrNull

val nonEmptyList = nonEmptyListOf(1,2,3)
val nonEmptyListFromList = listOf(1, 2, 3).toNonEmptyListOrNull()
val nullListFromEmptyList = emptyList<Int>().toNonEmptyListOrNull()


