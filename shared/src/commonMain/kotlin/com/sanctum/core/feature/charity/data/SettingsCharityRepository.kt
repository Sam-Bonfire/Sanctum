package com.sanctum.core.feature.charity.data

import com.russhwolf.settings.Settings
import com.sanctum.core.feature.charity.domain.CharityGoal
import com.sanctum.core.feature.charity.domain.CharityRecord
import com.sanctum.core.feature.charity.domain.CharityRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsCharityRepository(private val settings: Settings) : CharityRepository {

    private val keyCharityRecords = "charity_records"
    private val keyCharityGoal = "charity_monthly_goal"

    override suspend fun recordDonation(record: CharityRecord) {
        val currentRecords = getAllRecords().toMutableList()
        currentRecords.add(record)
        saveRecords(currentRecords)
    }

    override suspend fun getMonthlyRecords(year: Int, month: Int): List<CharityRecord> {
        val allRecords = getAllRecords()
        return allRecords.filter { record ->
            try {
                val instant = Instant.parse(record.dateIso)
                val dateTime = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                dateTime.year == year && dateTime.monthNumber == month
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getAllRecords(): List<CharityRecord> {
        val recordsJson = settings.getString(keyCharityRecords, "")
        return if (recordsJson.isEmpty()) {
            emptyList()
        } else {
            try {
                Json.decodeFromString(recordsJson)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun deleteRecord(id: String) {
        val currentRecords = getAllRecords().toMutableList()
        currentRecords.removeAll { it.id == id }
        saveRecords(currentRecords)
    }

    override suspend fun updateRecord(record: CharityRecord) {
        val currentRecords = getAllRecords().toMutableList()
        val index = currentRecords.indexOfFirst { it.id == record.id }
        if (index != -1) {
            currentRecords[index] = record
            saveRecords(currentRecords)
        }
    }

    override suspend fun setMonthlyGoal(goal: CharityGoal) {
        settings.putDouble(keyCharityGoal, goal.monthlyGoalAmount)
    }

    override suspend fun getMonthlyGoal(): CharityGoal {
        val amount = settings.getDouble(keyCharityGoal, 0.0)
        return CharityGoal(amount)
    }

    private fun saveRecords(records: List<CharityRecord>) {
        val json = Json.encodeToString(records)
        settings.putString(keyCharityRecords, json)
    }
}
