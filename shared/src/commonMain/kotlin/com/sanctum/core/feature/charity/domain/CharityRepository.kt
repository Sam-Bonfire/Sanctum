package com.sanctum.core.feature.charity.domain

interface CharityRepository {
    suspend fun recordDonation(record: CharityRecord)
    suspend fun getMonthlyRecords(year: Int, month: Int): List<CharityRecord>
    suspend fun getAllRecords(): List<CharityRecord>
    suspend fun deleteRecord(id: String)
    suspend fun updateRecord(record: CharityRecord)
    suspend fun setMonthlyGoal(goal: CharityGoal)
    suspend fun getMonthlyGoal(): CharityGoal
}
