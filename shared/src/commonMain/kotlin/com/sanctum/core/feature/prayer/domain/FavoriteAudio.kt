package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteAudio(
    val favorites: Map<String, Long> = emptyMap(),
) {
    fun addFavorite(audioKey: String, timestamp: Long): FavoriteAudio {
        val mutableFavorites = favorites.toMutableMap()
        mutableFavorites[audioKey] = timestamp
        return this.copy(favorites = mutableFavorites)
    }

    fun removeFavorite(audioKey: String): FavoriteAudio {
        val mutableFavorites = favorites.toMutableMap()
        mutableFavorites.remove(audioKey)
        return this.copy(favorites = mutableFavorites)
    }

    fun listFavorites(): List<String> {
        return favorites.entries
            .sortedBy { it.value }
            .map { it.key }
    }
}
