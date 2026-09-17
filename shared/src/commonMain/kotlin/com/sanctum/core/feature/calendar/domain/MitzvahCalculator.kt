package com.sanctum.core.feature.calendar.domain

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable

@Serializable
enum class MitzvahType {
    BAR,
    BAT,
}

fun mitzvahAge(type: MitzvahType): Int = if (type == MitzvahType.BAR) 13 else 12

fun mitzvahDate(birthDate: LocalDate, type: MitzvahType): LocalDate =
    birthDate.plus(DatePeriod(years = mitzvahAge(type)))

fun torahPortionIndex(mitzvahDate: LocalDate): Int {
    val epochDays = mitzvahDate.toEpochDays()
    return ((epochDays % 54) + 54).mod(54) + 1
}
