package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CrossfadeTest {

    private val config = CrossfadeConfig(1_000)

    @Test
    fun `validates duration range`() {
        assertTrue(config.isValid())
        assertFalse(CrossfadeConfig(100).isValid())
        assertFalse(CrossfadeConfig(20_000).isValid())
    }

    @Test
    fun `fades volumes linearly`() {
        assertEquals(1f, outgoingVolume(0, config))
        assertEquals(0.5f, outgoingVolume(500, config))
        assertEquals(0f, outgoingVolume(1_000, config))
        assertEquals(0f, incomingVolume(0, config))
        assertEquals(0.5f, incomingVolume(500, config))
        assertEquals(1f, incomingVolume(1_000, config))
    }
}
