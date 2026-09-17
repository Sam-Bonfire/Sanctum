package com.sanctum.core.core.designsystem.theme

import kotlinx.serialization.Serializable

@Serializable
data class HolidayTheme(
    val holidayId: String,
    val flavorId: String,
    val primaryHex: String,
    val secondaryHex: String,
    val month: Int,
    val startDay: Int,
    val endDay: Int,
)

fun HolidayTheme.isActive(month: Int, day: Int): Boolean {
    if (month != this.month) return false
    return day in startDay..endDay
}

fun themeForHoliday(
    themes: List<HolidayTheme>,
    flavorId: String,
    month: Int,
    day: Int,
): HolidayTheme? =
    themes.firstOrNull { it.flavorId == flavorId && it.isActive(month, day) }
