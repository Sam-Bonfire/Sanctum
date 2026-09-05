package com.sanctum.core.feature.charity.domain

import kotlin.random.Random

fun generateUUID(): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..16)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}
