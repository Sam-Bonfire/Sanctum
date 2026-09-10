package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReflectionTimerTest {

    @Test
    fun fullRunToCompletion() {
        val timer = ReflectionTimer(60_000)
        timer.start(1_000)
        assertFalse(timer.isComplete(30_000))
        assertEquals(31_000L, timer.remainingMs(30_000))
        assertTrue(timer.isComplete(61_000))
        assertEquals(0L, timer.remainingMs(61_000))
        assertEquals(1f, timer.progress(61_000))
        assertEquals(60_000L, timer.elapsedMs(99_000))
    }

    @Test
    fun pauseResumeAccumulates() {
        val timer = ReflectionTimer(10_000)
        timer.start(0)
        timer.pause(3_000)
        assertFalse(timer.isRunning)
        assertEquals(7_000L, timer.remainingMs(9_000))
        timer.start(9_000)
        assertEquals(0L, timer.remainingMs(16_000))
        assertTrue(timer.isComplete(16_000))
    }

    @Test
    fun startIsIdempotentWhileRunning() {
        val timer = ReflectionTimer(10_000)
        timer.start(0)
        timer.start(5_000)
        assertEquals(0L, timer.remainingMs(10_000))
    }

    @Test
    fun resetClears() {
        val timer = ReflectionTimer(10_000)
        timer.start(0)
        timer.pause(4_000)
        timer.reset()
        assertFalse(timer.isRunning)
        assertEquals(10_000L, timer.remainingMs(4_000))
        assertEquals(0f, timer.progress(4_000))
    }

    @Test
    fun clockSkewNeverGoesNegative() {
        val timer = ReflectionTimer(10_000)
        timer.start(5_000)
        assertEquals(10_000L, timer.remainingMs(1_000))
    }

    @Test
    fun startAfterCompleteStaysCompleteUntilReset() {
        val timer = ReflectionTimer(10_000)
        timer.start(0)
        assertTrue(timer.isComplete(10_000))
        timer.start(12_000)
        assertTrue(timer.isComplete(12_000))
        timer.reset()
        timer.start(12_000)
        assertFalse(timer.isComplete(12_000))
        assertEquals(10_000L, timer.remainingMs(12_000))
    }

    @Test
    fun rejectsNonPositiveTotal() {
        assertFailsWith<IllegalArgumentException> { ReflectionTimer(0) }
        assertFailsWith<IllegalArgumentException> { ReflectionTimer(-1) }
    }
}
