package com.sanctum.core.feature.reflection.domain

import kotlinx.serialization.Serializable

@Serializable
data class MeditationTrack(
    val id: String,
    val title: String,
    val durationMs: Long,
    val category: String = "guided",
    val audioKey: String = "",
)

@Serializable
data class MeditationGallery(
    val tracks: List<MeditationTrack> = emptyList(),
)

fun MeditationGallery.byCategory(category: String): List<MeditationTrack> =
    tracks.filter { it.category.equals(category, ignoreCase = true) }

fun MeditationGallery.shorterThan(durationMs: Long): List<MeditationTrack> =
    tracks.filter { it.durationMs <= durationMs }

fun MeditationGallery.find(id: String): MeditationTrack? = tracks.firstOrNull { it.id == id }
