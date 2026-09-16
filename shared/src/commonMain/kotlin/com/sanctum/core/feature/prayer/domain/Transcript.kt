package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
data class TranscriptSegment(
    val startMs: Long,
    val endMs: Long,
    val text: String,
)

@Serializable
data class Transcript(
    val audioId: String,
    val segments: List<TranscriptSegment> = emptyList(),
)

fun Transcript.activeSegmentAt(positionMs: Long): TranscriptSegment? =
    segments.firstOrNull { positionMs in it.startMs..<it.endMs }

fun Transcript.search(query: String): List<TranscriptSegment> {
    if (query.isBlank()) return emptyList()
    return segments.filter { it.text.contains(query, ignoreCase = true) }
}
