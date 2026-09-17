package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
data class DevotionalTrack(
    val id: String,
    val title: String,
    val deity: String = "",
    val audioKey: String = "",
    val lyrics: String = "",
)

@Serializable
data class DevotionalLibrary(
    val tracks: List<DevotionalTrack> = emptyList(),
)

fun DevotionalLibrary.forDeity(deity: String): List<DevotionalTrack> =
    tracks.filter { it.deity.equals(deity, ignoreCase = true) }

fun DevotionalLibrary.search(query: String): List<DevotionalTrack> {
    if (query.isBlank()) return emptyList()
    return tracks.filter {
        it.title.contains(query, ignoreCase = true) || it.lyrics.contains(query, ignoreCase = true)
    }
}
