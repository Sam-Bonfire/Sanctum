package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RingtoneExportTest {

    @Test
    fun `validates export spec`() {
        assertTrue(RingtoneExport("adhan_fajr", "Fajr Adhan").isValid())
        assertFalse(RingtoneExport("", "Title").isValid())
        assertFalse(RingtoneExport("key", "Title", durationMs = 60_000).isValid())
        assertFalse(RingtoneExport("key", "Title", startMs = -1).isValid())
    }

    @Test
    fun `builds safe file name`() {
        assertEquals("fajr_adhan.mp3", RingtoneExport("k", "Fajr Adhan!").fileName())
        assertEquals("tone.mp3", RingtoneExport("k", "!!!").fileName())
    }
}
