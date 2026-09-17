package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

const val MAX_RINGTONE_DURATION_MS = 30_000L

@Serializable
data class RingtoneExport(
    val audioKey: String,
    val title: String,
    val startMs: Long = 0,
    val durationMs: Long = MAX_RINGTONE_DURATION_MS,
)

fun RingtoneExport.isValid(): Boolean =
    audioKey.isNotBlank() && title.isNotBlank() && startMs >= 0 &&
        durationMs in 1..MAX_RINGTONE_DURATION_MS

fun RingtoneExport.fileName(): String {
    val safe = title.lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_')
    return "${safe.ifEmpty { "tone" }}.mp3"
}
