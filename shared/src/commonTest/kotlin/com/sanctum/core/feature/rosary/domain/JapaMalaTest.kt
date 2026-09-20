package com.sanctum.core.feature.rosary.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class JapaMalaTest {
    @Test
    fun testIncrement() {
        val mala = JapaMala()
        mala.increment()
        assertEquals(1, mala.beads)
        assertEquals(0, mala.laps)

        // increment up to 108
        repeat(107) { mala.increment() }
        assertEquals(0, mala.beads)
        assertEquals(1, mala.laps)
    }
}
