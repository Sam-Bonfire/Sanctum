package com.sanctum.core.feature.fasting.data

import com.russhwolf.settings.MapSettings
import com.sanctum.core.feature.fasting.domain.FastingDayRecord
import com.sanctum.core.feature.fasting.domain.FastingStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FastingRepositoryTest {

    private val settings = MapSettings()
    private val repository = FastingRepository(settings)

    @Test
    fun testSaveAndGetRecord() {
        val record = FastingDayRecord(
            dayOfRamadan = 15,
            status = FastingStatus.COMPLETED,
            notes = "Felt great today.",
        )

        repository.saveRecord(record)

        val retrievedRecord = repository.getRecord(15)
        assertEquals(record, retrievedRecord)
    }

    @Test
    fun testGetNonExistentRecord() {
        val retrievedRecord = repository.getRecord(5)
        assertNull(retrievedRecord)
    }

    @Test
    fun testUpdateExistingRecord() {
        // Initial save
        repository.saveRecord(FastingDayRecord(10, FastingStatus.MISSED, "Was sick."))

        // Update
        repository.saveRecord(FastingDayRecord(10, FastingStatus.EXEMPT, "Travel."))

        val updated = repository.getRecord(10)
        assertEquals(FastingStatus.EXEMPT, updated?.status)
        assertEquals("Travel.", updated?.notes)
    }

    @Test
    fun testGetAllRecordsContains30Days() {
        repository.saveRecord(FastingDayRecord(1, FastingStatus.COMPLETED, ""))
        repository.saveRecord(FastingDayRecord(30, FastingStatus.COMPLETED, "Last day!"))

        val allRecords = repository.getAllRecords()

        assertEquals(30, allRecords.size)
        assertEquals(FastingStatus.COMPLETED, allRecords[0].status)
        assertEquals(FastingStatus.COMPLETED, allRecords[29].status)
        assertEquals("Last day!", allRecords[29].notes)
        assertNull(allRecords[15].status) // Day 16 (index 15) is empty
    }
}
