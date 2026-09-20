package com.sanctum.core.feature.reading.domain

class CatchUpMode {
    fun calculateDailyTargets(totalUnits: Int, completedUnits: Int, daysLeft: Int, maxCatchUpPerDay: Int): List<Int> {
        val remainingUnits: Int = totalUnits - completedUnits
        if (remainingUnits <= 0) return if (daysLeft > 0) List(daysLeft) { 0 } else emptyList()
        val effectiveDaysLeft: Int = if (daysLeft > 0) daysLeft else 1
        val basePerDay: Int = remainingUnits / effectiveDaysLeft
        val remainder: Int = remainingUnits % effectiveDaysLeft
        val requiresMoreThanMax: Boolean = (basePerDay + if (remainder > 0) 1 else 0) > maxCatchUpPerDay
        if (requiresMoreThanMax || daysLeft <= 0) {
            val daysNeeded: Int = remainingUnits / maxCatchUpPerDay
            val leftover: Int = remainingUnits % maxCatchUpPerDay
            val list: MutableList<Int> = MutableList(daysNeeded) { maxCatchUpPerDay }
            if (leftover > 0) list.add(leftover)
            return list.toList()
        }
        val list: MutableList<Int> = MutableList(daysLeft) { basePerDay }
        for (i: Int in 0 until remainder) list[i] = list[i] + 1
        return list.toList()
    }
}
