package com.sanctum.core.feature.rosary.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RosaryTrackerTest {

    @Test
    fun buildsSixtyNineSteps() {
        val steps = buildRosarySteps(RosaryMysterySet.JOYFUL)
        assertEquals(69, steps.size)
    }

    @Test
    fun introAndClosingOrder() {
        val steps = buildRosarySteps(RosaryMysterySet.SORROWFUL)
        assertEquals(RosaryPrayer.SIGN_OF_CROSS, steps[0].prayer)
        assertEquals(RosaryPrayer.APOSTLES_CREED, steps[1].prayer)
        assertEquals(RosaryPrayer.OUR_FATHER, steps[2].prayer)
        assertEquals(RosaryPrayer.GLORY_BE, steps[6].prayer)
        assertEquals(RosaryPrayer.HAIL_HOLY_QUEEN, steps[67].prayer)
        assertEquals(RosaryPrayer.SIGN_OF_CROSS, steps[68].prayer)
    }

    @Test
    fun eachDecadeHasTwelveSteps() {
        val steps = buildRosarySteps(RosaryMysterySet.GLORIOUS)
        for (decade in 0..4) {
            val decadeSteps = steps.filter { it.decadeIndex == decade }
            assertEquals(12, decadeSteps.size)
            assertEquals(RosaryPrayer.OUR_FATHER, decadeSteps.first().prayer)
            assertEquals(RosaryPrayer.GLORY_BE, decadeSteps.last().prayer)
            assertEquals(10, decadeSteps.count { it.prayer == RosaryPrayer.HAIL_MARY })
        }
    }

    @Test
    fun weekdayMapping() {
        assertEquals(RosaryMysterySet.JOYFUL, mysterySetForWeekdayNumber(1))
        assertEquals(RosaryMysterySet.SORROWFUL, mysterySetForWeekdayNumber(2))
        assertEquals(RosaryMysterySet.GLORIOUS, mysterySetForWeekdayNumber(3))
        assertEquals(RosaryMysterySet.LUMINOUS, mysterySetForWeekdayNumber(4))
        assertEquals(RosaryMysterySet.SORROWFUL, mysterySetForWeekdayNumber(5))
        assertEquals(RosaryMysterySet.JOYFUL, mysterySetForWeekdayNumber(6))
        assertEquals(RosaryMysterySet.GLORIOUS, mysterySetForWeekdayNumber(7))
        assertFailsWith<IllegalArgumentException> { mysterySetForWeekdayNumber(0) }
        assertFailsWith<IllegalArgumentException> { mysterySetForWeekdayNumber(8) }
    }

    @Test
    fun trackerAdvancesToCompletion() {
        val tracker = RosaryTracker.forMystery(RosaryMysterySet.LUMINOUS)
        assertFalse(tracker.isComplete)
        assertEquals(0f, tracker.progress)
        repeat(69) { assertTrue(tracker.advance()) }
        assertTrue(tracker.isComplete)
        assertEquals(1f, tracker.progress)
        assertNull(tracker.current)
        assertFalse(tracker.advance())
    }

    @Test
    fun trackerRetreatAndReset() {
        val tracker = RosaryTracker.forMystery(RosaryMysterySet.JOYFUL)
        assertNotNull(tracker.current)
        tracker.advance()
        tracker.advance()
        assertEquals(2, tracker.completedCount)
        tracker.retreat()
        assertEquals(1, tracker.completedCount)
        tracker.reset()
        assertEquals(0, tracker.completedCount)
        assertFalse(tracker.isComplete)
    }

    @Test
    fun rejectsEmptySteps() {
        assertFailsWith<IllegalArgumentException> { RosaryTracker(emptyList()) }
    }
}
