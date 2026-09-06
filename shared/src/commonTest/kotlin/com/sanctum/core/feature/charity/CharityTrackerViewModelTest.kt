package com.sanctum.core.feature.charity

import com.sanctum.core.feature.charity.domain.CharityGoal
import com.sanctum.core.feature.charity.domain.CharityRecord
import com.sanctum.core.feature.charity.domain.CharityRepository
import com.sanctum.core.feature.charity.presentation.CharityTrackerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FakeCharityRepository : CharityRepository {
    var records = mutableListOf<CharityRecord>()
    var goal = CharityGoal(0.0)

    override suspend fun recordDonation(record: CharityRecord) {
        records.add(record)
    }

    override suspend fun getMonthlyRecords(year: Int, month: Int): List<CharityRecord> {
        return records
    }

    override suspend fun getAllRecords(): List<CharityRecord> {
        return records
    }

    override suspend fun deleteRecord(id: String) {
        records.removeAll { it.id == id }
    }

    override suspend fun updateRecord(record: CharityRecord) {
        val index = records.indexOfFirst { it.id == record.id }
        if (index != -1) {
            records[index] = record
        }
    }

    override suspend fun setMonthlyGoal(goal: CharityGoal) {
        this.goal = goal
    }

    override suspend fun getMonthlyGoal(): CharityGoal {
        return goal
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CharityTrackerViewModelTest {

    private lateinit var repository: FakeCharityRepository
    private lateinit var viewModel: CharityTrackerViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeCharityRepository()
        viewModel = CharityTrackerViewModel(repository)
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialLoad() = runTest {
        repository.setMonthlyGoal(CharityGoal(1000.0))
        repository.recordDonation(CharityRecord("1", 200.0, "", com.sanctum.core.feature.charity.domain.CharityCategory.ZAKAT, null))
        repository.recordDonation(CharityRecord("2", 300.0, "", com.sanctum.core.feature.charity.domain.CharityCategory.SADAQAH, null))

        viewModel.loadData()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.records.size)
        assertEquals(500.0, state.summary.totalGiven)
        assertEquals(1000.0, state.summary.goalAmount)
    }
}
