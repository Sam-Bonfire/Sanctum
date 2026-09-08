package com.sanctum.core.feature.habits.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class HabitTreeTest {

    @Test
    fun stageBoundaries() {
        val expected = mapOf(
            0 to GrowthStage.SEED,
            1 to GrowthStage.SPROUT,
            6 to GrowthStage.SPROUT,
            7 to GrowthStage.SAPLING,
            13 to GrowthStage.SAPLING,
            14 to GrowthStage.TREE,
            29 to GrowthStage.TREE,
            30 to GrowthStage.BLOOMING,
            59 to GrowthStage.BLOOMING,
            60 to GrowthStage.EVERGREEN,
            365 to GrowthStage.EVERGREEN,
        )
        for ((streak, stage) in expected) {
            assertEquals(stage, HabitTree.growthForStreak(streak).stage, "streak $streak")
        }
    }

    @Test
    fun progressWithinStage() {
        // Sprout spans 1..6: day 4 is halfway.
        val mid = HabitTree.growthForStreak(4)
        assertEquals(3, mid.daysToNextStage)
        assertEquals(0.5f, mid.stageProgress ?: -1f, 0.01f)
        // First day of a stage starts at zero.
        assertEquals(0f, HabitTree.growthForStreak(7).stageProgress ?: -1f)
    }

    @Test
    fun fullyGrownHasNoNextStage() {
        val maxed = HabitTree.growthForStreak(200)
        assertEquals(GrowthStage.EVERGREEN, maxed.stage)
        assertNull(maxed.daysToNextStage)
        assertNull(maxed.stageProgress)
    }

    @Test
    fun rejectsNegativeStreak() {
        assertFailsWith<IllegalArgumentException> { HabitTree.growthForStreak(-1) }
    }
}
