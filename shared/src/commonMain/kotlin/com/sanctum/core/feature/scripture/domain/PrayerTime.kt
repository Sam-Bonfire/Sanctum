package com.sanctum.core.feature.scripture.domain

data class PrayerTime(val name: String, val time: String, val amPm: String, val isCurrent: Boolean = false)
