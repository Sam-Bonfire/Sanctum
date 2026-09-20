package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class FavoriteAudioTest {

    @Test
    fun testAddFavorite() {
        val initial = FavoriteAudio()
        val updated = initial.addFavorite("audio1", 1000L)

        assertEquals(1, updated.favorites.size)
        assertEquals(1000L, updated.favorites["audio1"])
    }

    @Test
    fun testAddExistingFavoriteUpdatesTimestamp() {
        val initial = FavoriteAudio(mapOf("audio1" to 1000L))
        val updated = initial.addFavorite("audio1", 2000L)

        assertEquals(1, updated.favorites.size)
        assertEquals(2000L, updated.favorites["audio1"])
    }

    @Test
    fun testRemoveFavorite() {
        val initial = FavoriteAudio(mapOf("audio1" to 1000L, "audio2" to 2000L))
        val updated = initial.removeFavorite("audio1")

        assertEquals(1, updated.favorites.size)
        assertEquals(null, updated.favorites["audio1"])
        assertEquals(2000L, updated.favorites["audio2"])
    }

    @Test
    fun testRemoveNonExistingFavoriteDoesNothing() {
        val initial = FavoriteAudio(mapOf("audio1" to 1000L))
        val updated = initial.removeFavorite("audio2")

        assertEquals(1, updated.favorites.size)
        assertEquals(1000L, updated.favorites["audio1"])
    }

    @Test
    fun testListFavoritesSortedByTimestamp() {
        val initial = FavoriteAudio(
            mapOf(
                "audio3" to 3000L,
                "audio1" to 1000L,
                "audio2" to 2000L,
            ),
        )

        val list = initial.listFavorites()

        assertEquals(listOf("audio1", "audio2", "audio3"), list)
    }

    @Test
    fun testListFavoritesEmpty() {
        val initial = FavoriteAudio()

        val list = initial.listFavorites()

        assertEquals(emptyList(), list)
    }
}
