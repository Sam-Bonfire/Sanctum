package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class MemorizationRound(
    val verseText: String,
    val hiddenWordIndexes: Set<Int> = emptySet(),
)

fun MemorizationRound.words(): List<String> = verseText.split(" ").filter { it.isNotBlank() }

fun MemorizationRound.nextRound(): MemorizationRound {
    val total = words().size
    val visible = (0 until total).filter { it !in hiddenWordIndexes }
    if (visible.isEmpty()) return this
    return copy(hiddenWordIndexes = hiddenWordIndexes + visible.first())
}

fun MemorizationRound.displayWords(): List<String> =
    words().mapIndexed { index, word -> if (index in hiddenWordIndexes) "…" else word }

fun MemorizationRound.score(attempt: List<String>): Int {
    val expected = words()
    return expected.indices.count { attempt.getOrNull(it) == expected[it] }
}
