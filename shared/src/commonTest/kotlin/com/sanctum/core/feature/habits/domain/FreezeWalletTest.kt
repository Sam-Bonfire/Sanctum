package com.sanctum.core.feature.habits.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FreezeWalletTest {

    @Test
    fun spendAndEarnThroughFreezeDay() {
        val wallet = FreezeWallet(1)
        assertTrue(wallet.canFreeze())
        val frozen = wallet.freezeDay(2, setOf(0, 1), emptySet(), 3)
        assertEquals(0, frozen.wallet.balance)
        assertFalse(frozen.wallet.canFreeze())
        assertEquals(2, frozen.wallet.earn(2).balance)
    }

    @Test
    fun rejectsOverspendAndBadInput() {
        assertFailsWith<IllegalArgumentException> {
            FreezeWallet(0).freezeDay(1, emptySet(), emptySet(), 3)
        }
        assertFailsWith<IllegalArgumentException> { FreezeWallet(-1) }
        assertFailsWith<IllegalArgumentException> { FreezeWallet(1).earn(0) }
        assertFailsWith<IllegalArgumentException> {
            FreezeWallet(1).freezeDay(-1, emptySet(), emptySet(), 3)
        }
        assertFailsWith<IllegalArgumentException> {
            FreezeWallet(1).freezeDay(5, emptySet(), emptySet(), 3)
        }
    }

    @Test
    fun rejectsRedundantFreeze() {
        assertFailsWith<IllegalArgumentException> {
            FreezeWallet(2).freezeDay(3, setOf(3), emptySet(), 5)
        }
        assertFailsWith<IllegalArgumentException> {
            FreezeWallet(2).freezeDay(3, emptySet(), setOf(3), 5)
        }
    }

    @Test
    fun frozenDayJoinsFrozenSet() {
        val wallet = FreezeWallet(1)
        val result = wallet.freezeDay(2, setOf(0, 1, 3), emptySet(), 3)
        assertEquals(0, result.wallet.balance)
        assertEquals(setOf(2), result.frozenDays)
        // Bridging (active + frozen -> unbroken streak) is covered by
        // StreakTrackerTest once S-140 merges into the same package.
    }
}
