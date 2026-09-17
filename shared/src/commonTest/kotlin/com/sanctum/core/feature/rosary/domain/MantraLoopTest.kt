package com.sanctum.core.feature.rosary.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MantraLoopTest {

    @Test
    fun `counts chants to completion`() {
        val loop = MantraLoop("Om", 3).chant().chant()
        assertFalse(loop.isComplete())
        assertEquals(1, loop.remaining())
        assertTrue(loop.chant().isComplete())
    }

    @Test
    fun `stays complete and rejects empty loops`() {
        val done = MantraLoop("Om", 1, 1)
        assertEquals(done, done.chant())
        assertFailsWith<IllegalArgumentException> { MantraLoop("Om", 0).chant() }
    }
}
