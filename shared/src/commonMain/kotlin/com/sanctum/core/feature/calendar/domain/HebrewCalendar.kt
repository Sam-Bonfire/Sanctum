package com.sanctum.core.feature.calendar.domain

/**
 * Hebrew (Jewish) calendar logic and Gregorian converter.
 *
 * Algorithm: "Calendrical Calculations" (Dershowitz & Reingold) via absolute
 * day numbers (Rata Die: day 1 = Monday, 1 Jan 1 CE proleptic Gregorian).
 * All arithmetic uses [Long] — intermediate products exceed 32-bit range.
 *
 * Months are numbered from Nisan (1 = Nisan … 7 = Tishri … 12 = Adar,
 * 13 = Adar II in leap years), matching the calendar literature. Use
 * [monthName] for display.
 */
object HebrewCalendar {

    const val NISAN = 1
    const val IYYAR = 2
    const val SIVAN = 3
    const val TAMUZ = 4
    const val AV = 5
    const val ELUL = 6
    const val TISHRI = 7
    const val HESHVAN = 8
    const val KISLEV = 9
    const val TEVET = 10
    const val SHEVAT = 11
    const val ADAR = 12
    const val ADAR_II = 13

    /** Absolute day of 1 Tishri 1 AM (Monday). */
    private const val EPOCH: Long = 347998L

    fun isLeapYear(year: Int): Boolean = ((7L * year + 1) % 19) < 7

    fun monthsInYear(year: Int): Int = if (isLeapYear(year)) 13 else 12

    /** Total days in [year]: one of 353/354/355 (common) or 383/384/385 (leap). */
    fun daysInYear(year: Int): Long = newYearAbsolute(year + 1) - newYearAbsolute(year)

    fun daysInMonth(year: Int, month: Int): Int {
        require(month in NISAN..monthsInYear(year)) { "month $month out of range for year $year" }
        return when (month) {
            IYYAR, TAMUZ, ELUL, TEVET, ADAR_II -> 29
            ADAR -> if (isLeapYear(year)) 30 else 29
            HESHVAN -> if (daysInYear(year) % 10 == 5L) 30 else 29
            KISLEV -> if (daysInYear(year) % 10 == 3L) 29 else 30
            else -> 30
        }
    }

    fun monthName(month: Int, year: Int): String {
        val leap = isLeapYear(year)
        return when (month) {
            NISAN -> "Nisan"
            IYYAR -> "Iyyar"
            SIVAN -> "Sivan"
            TAMUZ -> "Tamuz"
            AV -> "Av"
            ELUL -> "Elul"
            TISHRI -> "Tishri"
            HESHVAN -> "Heshvan"
            KISLEV -> "Kislev"
            TEVET -> "Tevet"
            SHEVAT -> "Shevat"
            ADAR -> if (leap) "Adar I" else "Adar"
            ADAR_II -> {
                require(leap) { "Adar II exists only in leap years (got $year)" }
                "Adar II"
            }
            else -> throw IllegalArgumentException("month $month out of range")
        }
    }

    fun fromGregorian(year: Int, month: Int, day: Int): HebrewDate =
        fromAbsolute(gregorianToAbsolute(year, month, day))

    fun gregorianFromHebrew(date: HebrewDate): GregorianDate =
        absoluteToGregorian(date.toAbsolute())

    fun gregorianToAbsolute(year: Int, month: Int, day: Int): Long {
        require(year >= 1 && month in 1..12) { "invalid Gregorian date" }
        require(day in 1..gregorianMonthLength(year, month)) { "day $day out of range for $month/$year" }
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        return day + (153L * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045L
    }

    fun absoluteToGregorian(absolute: Long): GregorianDate {
        require(absolute >= 1) { "absolute day must be >= 1" }
        val a = absolute + 32044
        val b = (4 * a + 3) / 146097
        val c = a - (146097L * b) / 4
        val d = (4 * c + 3) / 1461
        val e = c - (1461L * d) / 4
        val m = (5 * e + 2) / 153
        val day = (e - (153L * m + 2) / 5 + 1).toInt()
        val month = (m + 3 - 12 * (m / 10)).toInt()
        val year = (100 * b + d - 4800 + m / 10).toInt()
        return GregorianDate(year, month, day)
    }

    fun toAbsolute(year: Int, month: Int, day: Int): Long {
        require(month in NISAN..monthsInYear(year)) { "month $month out of range for year $year" }
        require(day in 1..daysInMonth(year, month)) { "day $day out of range" }
        var abs = newYearAbsolute(year) + day - 1
        if (month < TISHRI) {
            for (m in TISHRI..monthsInYear(year)) abs += daysInMonth(year, m)
            for (m in NISAN until month) abs += daysInMonth(year, m)
        } else {
            for (m in TISHRI until month) abs += daysInMonth(year, m)
        }
        return abs
    }

    fun fromAbsolute(absolute: Long): HebrewDate {
        require(absolute >= 1) { "absolute day must be >= 1" }
        val approx = ((absolute - EPOCH) * 98496L) / 35975351L
        var year = (approx - 1).toInt()
        while (newYearAbsolute(year + 1) <= absolute) year++
        while (newYearAbsolute(year) > absolute) year--
        var month = if (absolute < toAbsolute(year, NISAN, 1)) TISHRI else NISAN
        while (absolute > toAbsolute(year, month, daysInMonth(year, month))) month++
        val day = (absolute - toAbsolute(year, month, 1) + 1).toInt()
        return HebrewDate(year, month, day)
    }

    private fun moladDelay(year: Int): Long {
        val months = ((235L * year) - 234) / 19
        val parts = 12084 + 13753 * months
        var day = months * 29 + parts / 25920
        if ((3 * (day + 1)) % 7 < 3) day++
        return day
    }

    /** Dehiyyot (postponement) adjustment for Rosh Hashanah. */
    private fun newYearDelay(year: Int): Long {
        val last = moladDelay(year - 1)
        val present = moladDelay(year)
        val next = moladDelay(year + 1)
        return when {
            next - present == 356L -> 2
            present - last == 382L -> 1
            else -> 0
        }
    }

    private fun newYearAbsolute(year: Int): Long =
        EPOCH + moladDelay(year) + newYearDelay(year)

    private fun isGregorianLeap(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

    private fun gregorianMonthLength(year: Int, month: Int): Int = when (month) {
        2 -> if (isGregorianLeap(year)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
}

data class HebrewDate(val year: Int, val month: Int, val day: Int) {
    init {
        require(year >= 1) { "year must be >= 1" }
        require(month in HebrewCalendar.NISAN..HebrewCalendar.monthsInYear(year)) {
            "month $month out of range for year $year"
        }
        require(day in 1..HebrewCalendar.daysInMonth(year, month)) {
            "day $day out of range for month $month year $year"
        }
    }

    fun toAbsolute(): Long = HebrewCalendar.toAbsolute(year, month, day)

    fun displayName(): String = "$day ${HebrewCalendar.monthName(month, year)} $year"
}

data class GregorianDate(val year: Int, val month: Int, val day: Int)
