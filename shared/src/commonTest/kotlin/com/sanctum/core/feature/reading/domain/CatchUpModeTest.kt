package com.sanctum.core.feature.reading.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CatchUpModeTest {
    private val catchUpMode: CatchUpMode = CatchUpMode()

    @Test
    fun testCatchUp() {
        var targets: List<Int> = catchUpMode.calculateDailyTargets(20, 10, 5, 3)
        assertEquals(listOf(2, 2, 2, 2, 2), targets)

        targets = catchUpMode.calculateDailyTargets(20, 9, 5, 3)
        assertEquals(listOf(3, 2, 2, 2, 2), targets)

        targets = catchUpMode.calculateDailyTargets(20, 10, 2, 3)
        assertEquals(listOf(3, 3, 3, 1), targets)

        targets = catchUpMode.calculateDailyTargets(20, 10, 0, 4)
        assertEquals(listOf(4, 4, 2), targets)

        targets = catchUpMode.calculateDailyTargets(20, 10, -2, 4)
        assertEquals(listOf(4, 4, 2), targets)

        targets = catchUpMode.calculateDailyTargets(20, 20, 5, 3)
        assertEquals(listOf(0, 0, 0, 0, 0), targets)

        targets = catchUpMode.calculateDailyTargets(20, 20, 0, 3)
        assertEquals(emptyList(), targets)

        targets = catchUpMode.calculateDailyTargets(20, 25, 3, 3)
        assertEquals(listOf(0, 0, 0), targets)
    }
}
