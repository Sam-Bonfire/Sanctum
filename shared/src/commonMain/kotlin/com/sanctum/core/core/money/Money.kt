package com.sanctum.core.core.money

import kotlin.math.abs
import kotlin.math.roundToLong

// ponytail: Long minor-units (cents) for stored money; Double only for market price inputs
typealias MinorUnits = Long

fun Double.toMinorUnits(): MinorUnits = (this * 100).roundToLong()

fun MinorUnits.formatMinor(): String {
    val abs = abs(this)
    return "${if (this < 0) "-" else ""}${abs / 100}.${(abs % 100).toString().padStart(2, '0')}"
}

fun String.parseMinorUnits(): MinorUnits? = toDoubleOrNull()?.toMinorUnits()
