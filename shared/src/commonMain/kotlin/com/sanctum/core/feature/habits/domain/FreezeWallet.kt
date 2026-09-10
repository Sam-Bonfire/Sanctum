package com.sanctum.core.feature.habits.domain

/**
 * Streak-freeze token wallet. Spending a token marks one missed day as
 * frozen; frozen days join the active set so [StreakTracker] bridges the gap.
 * Earn rules (purchase/streak rewards) and balance caps are product decisions
 * for the store phase — the wallet only guards against overspending.
 */
data class FreezeWallet(val balance: Int) {
    init {
        require(balance >= 0) { "balance must be >= 0" }
    }

    fun canFreeze(): Boolean = balance > 0

    fun earn(tokens: Int = 1): FreezeWallet {
        require(tokens > 0) { "tokens must be > 0" }
        return copy(balance = balance + tokens)
    }

    private fun spend(): FreezeWallet {
        require(canFreeze()) { "no freeze tokens left" }
        return copy(balance = balance - 1)
    }

    /**
     * Freezes [day]: spends one token and returns the updated wallet plus the
     * frozen-days set with [day] added. Days already active need no freeze;
     * future days (past [todayIndex]) cannot be frozen.
     */
    fun freezeDay(day: Int, activeDays: Set<Int>, frozenDays: Set<Int>, todayIndex: Int): FreezeResult {
        require(day in 0..todayIndex) { "day $day must be within 0..$todayIndex" }
        require(day !in activeDays) { "day $day is already active" }
        require(day !in frozenDays) { "day $day is already frozen" }
        return FreezeResult(spend(), frozenDays + day)
    }
}

data class FreezeResult(
    val wallet: FreezeWallet,
    val frozenDays: Set<Int>,
)
