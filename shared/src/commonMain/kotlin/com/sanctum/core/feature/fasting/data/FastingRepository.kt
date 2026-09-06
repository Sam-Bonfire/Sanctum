package com.sanctum.core.feature.fasting.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.sanctum.core.feature.fasting.domain.FastingDayRecord
import com.sanctum.core.feature.fasting.domain.FastingStatus

class FastingRepository(private val settings: Settings) {

    private val keyPrefixStatus = "fasting_record_status_"
    private val keyPrefixNotes = "fasting_record_notes_"

    fun getRecord(dayOfRamadan: Int): FastingDayRecord? {
        val statusStr = settings.getStringOrNull("$keyPrefixStatus$dayOfRamadan")
        val notes = settings.getString("$keyPrefixNotes$dayOfRamadan", "")

        if (statusStr == null && notes.isEmpty()) {
            return null
        }

        val status = try {
            if (statusStr != null) FastingStatus.valueOf(statusStr) else null
        } catch (e: IllegalArgumentException) {
            null
        }

        return FastingDayRecord(dayOfRamadan, status, notes)
    }

    fun saveRecord(record: FastingDayRecord) {
        if (record.status != null) {
            settings["$keyPrefixStatus${record.dayOfRamadan}"] = record.status.name
        } else {
            settings.remove("$keyPrefixStatus${record.dayOfRamadan}")
        }

        if (record.notes.isNotEmpty()) {
            settings["$keyPrefixNotes${record.dayOfRamadan}"] = record.notes
        } else {
            settings.remove("$keyPrefixNotes${record.dayOfRamadan}")
        }
    }

    fun getAllRecords(): List<FastingDayRecord> {
        val records = mutableListOf<FastingDayRecord>()
        for (day in 1..30) {
            val record = getRecord(day)
            if (record != null) {
                records.add(record)
            } else {
                records.add(FastingDayRecord(day, null, ""))
            }
        }
        return records
    }
}
