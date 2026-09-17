package com.sanctum.core.core.design

import kotlin.test.Test
import kotlin.test.assertEquals

class FaithProfileTest {

    private val set = FaithProfileSet(
        listOf(
            FaithProfile("islam", "Nūr", true),
            FaithProfile("jewish", "Shalom"),
        ),
    )

    @Test
    fun `lists active profiles`() {
        assertEquals(listOf("islam"), set.active().map { it.flavorId })
    }

    @Test
    fun `toggles and activates exclusively`() {
        assertEquals(listOf("islam", "jewish"), set.toggle("jewish").active().map { it.flavorId })
        assertEquals(listOf("jewish"), set.activateOnly("jewish").active().map { it.flavorId })
    }
}
