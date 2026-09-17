package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class GdprExport(
    val exportedAtMs: Long,
    val payload: BackupPayload,
)

fun GdprExport.categories(): Map<String, Int> = mapOf(
    "bookmarks" to payload.bookmarks.size,
    "notes" to payload.notes.size,
    "highlights" to payload.highlights.size,
    "journalEntries" to payload.journalEntries.size,
)

fun GdprExport.totalRecords(): Int = categories().values.sum()

fun GdprExport.isEmpty(): Boolean = totalRecords() == 0
