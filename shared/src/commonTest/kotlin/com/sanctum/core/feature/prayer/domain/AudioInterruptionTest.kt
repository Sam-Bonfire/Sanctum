package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AudioInterruptionTest {

    @Test
    fun `phone calls pause`() {
        assertEquals(
            InterruptionAction.PAUSE,
            resolveInterruption(AudioInterruption(InterruptionType.PHONE_CALL)),
        )
    }

    @Test
    fun `navigation and notifications duck`() {
        assertEquals(
            InterruptionAction.DUCK,
            resolveInterruption(AudioInterruption(InterruptionType.NAVIGATION)),
        )
        assertEquals(
            InterruptionAction.DUCK,
            resolveInterruption(AudioInterruption(InterruptionType.NOTIFICATION)),
        )
    }

    @Test
    fun `other stops when resume impossible`() {
        assertEquals(
            InterruptionAction.STOP,
            resolveInterruption(AudioInterruption(InterruptionType.OTHER, canResume = false)),
        )
        assertEquals(
            InterruptionAction.PAUSE,
            resolveInterruption(AudioInterruption(InterruptionType.OTHER, canResume = true)),
        )
    }
}
