package com.sanctum.core.core.notifications

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BackgroundScheduleTest {

    @Test
    fun `validates intervals`() {
        assertTrue(BackgroundSchedule(BackgroundTask.SYNC, 12).isValid())
        assertFalse(BackgroundSchedule(BackgroundTask.SYNC, 0).isValid())
        assertFalse(BackgroundSchedule(BackgroundTask.SYNC, 200).isValid())
    }

    @Test
    fun `provides defaults`() {
        assertEquals(3, defaultSchedules().size)
        assertTrue(defaultSchedules().all { it.isValid() })
    }
}
