package com.sanctum.core.feature.reading.data

import com.russhwolf.settings.MapSettings
import com.sanctum.core.feature.reading.domain.GetEnrolledPlansUseCase
import com.sanctum.core.feature.reading.domain.ToggleCheckpointCompletedUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsReadingPlanRepositoryTest {

    private lateinit var settings: MapSettings
    private lateinit var repository: SettingsReadingPlanRepository

    @BeforeTest
    fun setUp() {
        settings = MapSettings()
        repository = SettingsReadingPlanRepository(settings)
    }

    @Test
    fun testGetAvailablePlans() {
        val plans = repository.getAvailablePlans()
        assertTrue(plans.any { it.id == "nt_in_a_year" })
        val first = plans.first { it.id == "nt_in_a_year" }
        assertEquals(365, first.dayCount)
    }

    @Test
    fun testBibleYearPlanRefsMatchDayCount() {
        val bible = repository.getAvailablePlans().find { it.id == "bible_in_a_year" }
        assertTrue(bible != null)
        assertEquals(bible.dayCount, bible.verseRefs.size)
        assertEquals("Chapters 1-4", bible.verseRefs.first())
        assertTrue(bible.verseRefs.last().endsWith("1189"))
    }

    @Test
    fun testEnrollAndUnenroll() {
        repository.enroll("psalms_30")

        assertTrue(repository.getEnrolledPlanIds().contains("psalms_30"))
        assertTrue(repository.getProgress("psalms_30").enrolledAt > 0L)

        repository.unenroll("psalms_30")

        assertFalse(repository.getEnrolledPlanIds().contains("psalms_30"))
        assertEquals(0L, repository.getProgress("psalms_30").enrolledAt)
    }

    @Test
    fun testCheckpointToggle() {
        val key = "psalms_30_day0_cp0"

        repository.setCheckpointCompleted("psalms_30", key, true)
        assertTrue(repository.isCheckpointCompleted("psalms_30", key))

        repository.setCheckpointCompleted("psalms_30", key, false)
        assertFalse(repository.isCheckpointCompleted("psalms_30", key))
    }

    @Test
    fun testGetEnrolledPlansUseCaseSetsState() {
        repository.enroll("nt_in_a_year")
        val useCase = GetEnrolledPlansUseCase(repository)
        val states = useCase()

        assertEquals(1, states.size)
        assertEquals("nt_in_a_year", states[0].plan.id)
        assertTrue(states[0].isEnrolled)
        assertEquals(0f, states[0].completionPercent)
    }

    @Test
    fun testToggleCheckpointMarksDayComplete() {
        repository.enroll("psalms_30")
        val useCase = ToggleCheckpointCompletedUseCase(repository)

        (0 until 5).forEach { idx ->
            useCase("psalms_30", "psalms_30_day0_cp$idx", true)
        }

        assertTrue(repository.getProgress("psalms_30").completedDays.contains(0))
    }
}
