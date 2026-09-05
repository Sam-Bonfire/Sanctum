package com.sanctum.core.feature.scripture.domain.memorization

import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MemorizationStateRepositoryTest {

    @Test
    fun testDefaultMasteryLevel() {
        val settings = MapSettings()
        val repo = MemorizationStateRepository(settings)

        assertEquals(MemorizationDifficulty.LEVEL_0_READ, repo.getMasteryLevel("v1"))
        assertFalse(repo.isMastered("v1"))
    }

    @Test
    fun testSetAndGetMasteryLevel() {
        val settings = MapSettings()
        val repo = MemorizationStateRepository(settings)

        repo.setMasteryLevel("v1", MemorizationDifficulty.LEVEL_2_HALF_BLANK)
        assertEquals(MemorizationDifficulty.LEVEL_2_HALF_BLANK, repo.getMasteryLevel("v1"))

        repo.setMasteryLevel("v2", MemorizationDifficulty.LEVEL_3_FULL_BLANK)
        assertEquals(MemorizationDifficulty.LEVEL_3_FULL_BLANK, repo.getMasteryLevel("v2"))
        assertTrue(repo.isMastered("v2"))
    }

    @Test
    fun testToggleMastered() {
        val settings = MapSettings()
        val repo = MemorizationStateRepository(settings)

        repo.toggleMastered("v1")
        assertEquals(MemorizationDifficulty.LEVEL_3_FULL_BLANK, repo.getMasteryLevel("v1"))
        assertTrue(repo.isMastered("v1"))

        repo.toggleMastered("v1")
        assertEquals(MemorizationDifficulty.LEVEL_0_READ, repo.getMasteryLevel("v1"))
        assertFalse(repo.isMastered("v1"))
    }
}
