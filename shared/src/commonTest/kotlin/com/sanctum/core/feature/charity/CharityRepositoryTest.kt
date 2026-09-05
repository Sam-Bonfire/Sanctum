package com.sanctum.core.feature.charity

import com.russhwolf.settings.MapSettings
import com.sanctum.core.feature.charity.data.SettingsCharityRepository
import com.sanctum.core.feature.charity.domain.CharityGoal
import com.sanctum.core.feature.charity.domain.CharityRecord
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CharityRepositoryTest {

    private lateinit var settings: MapSettings
    private lateinit var repository: SettingsCharityRepository

    @BeforeTest
    fun setup() {
        settings = MapSettings()
        repository = SettingsCharityRepository(settings)
    }

    @Test
    fun testRecordDonationAndRetrieve() = runTest {
        val record = CharityRecord(
            id = "1",
            amount = 100.0,
            dateIso = "2023-10-01T12:00:00Z",
            categoryId = com.sanctum.core.feature.charity.domain.CharityCategory.ZAKAT,
            privateNotes = "Test note",
        )

        repository.recordDonation(record)

        val records = repository.getAllRecords()
        assertEquals(1, records.size)
        assertEquals(record, records.first())
    }

    @Test
    fun testSetAndGetGoal() = runTest {
        val goal = CharityGoal(500.0)
        repository.setMonthlyGoal(goal)

        val retrievedGoal = repository.getMonthlyGoal()
        assertEquals(500.0, retrievedGoal.monthlyGoalAmount)
    }
}
