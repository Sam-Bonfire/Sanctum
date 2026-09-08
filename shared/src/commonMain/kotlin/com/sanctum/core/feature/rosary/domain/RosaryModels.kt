package com.sanctum.core.feature.rosary.domain

enum class RosaryPrayer(val audioKey: String) {
    SIGN_OF_CROSS("sign_of_cross"),
    APOSTLES_CREED("apostles_creed"),
    OUR_FATHER("our_father"),
    HAIL_MARY("hail_mary"),
    GLORY_BE("glory_be"),
    HAIL_HOLY_QUEEN("hail_holy_queen"),
}

enum class RosaryMysterySet(
    val displayName: String,
    val mysteries: List<String>,
) {
    JOYFUL(
        "Joyful",
        listOf(
            "The Annunciation",
            "The Visitation",
            "The Nativity",
            "The Presentation",
            "The Finding in the Temple",
        ),
    ),
    LUMINOUS(
        "Luminous",
        listOf(
            "The Baptism of Jesus",
            "The Wedding at Cana",
            "The Proclamation of the Kingdom",
            "The Transfiguration",
            "The Institution of the Eucharist",
        ),
    ),
    SORROWFUL(
        "Sorrowful",
        listOf(
            "The Agony in the Garden",
            "The Scourging at the Pillar",
            "The Crowning with Thorns",
            "The Carrying of the Cross",
            "The Crucifixion",
        ),
    ),
    GLORIOUS(
        "Glorious",
        listOf(
            "The Resurrection",
            "The Ascension",
            "The Descent of the Holy Spirit",
            "The Assumption of Mary",
            "The Coronation of Mary",
        ),
    ),
}

data class RosaryStep(
    val prayer: RosaryPrayer,
    val label: String,
    /** 0..4 for decades, null for intro/closing beads. */
    val decadeIndex: Int?,
    /** 0..9 within a decade for Hail Marys, null otherwise. */
    val beadInDecade: Int?,
)

/**
 * Traditional weekday mapping (Monday = 1 .. Sunday = 7):
 * Mon/Sat Joyful, Tue/Fri Sorrowful, Wed/Sun Glorious, Thu Luminous.
 *
 * Simplification: ignores liturgical seasons (Sundays in Advent/Christmas use
 * Joyful, Sundays in Lent use Sorrowful). Seasonal override needs a liturgical
 * calendar source — deferred until the prayer UI phase (S-17 follow-up).
 */
fun mysterySetForWeekdayNumber(dayNumber: Int): RosaryMysterySet {
    require(dayNumber in 1..7) { "dayNumber must be 1 (Mon) .. 7 (Sun)" }
    return when (dayNumber) {
        1, 6 -> RosaryMysterySet.JOYFUL
        2, 5 -> RosaryMysterySet.SORROWFUL
        4 -> RosaryMysterySet.LUMINOUS
        else -> RosaryMysterySet.GLORIOUS
    }
}

/**
 * Linear bead sequence: 7 intro beads + 5 decades x 12 + 2 closing = 69 steps.
 */
fun buildRosarySteps(set: RosaryMysterySet): List<RosaryStep> {
    val steps = ArrayList<RosaryStep>(69)
    steps.add(RosaryStep(RosaryPrayer.SIGN_OF_CROSS, "Sign of the Cross", null, null))
    steps.add(RosaryStep(RosaryPrayer.APOSTLES_CREED, "Apostles' Creed", null, null))
    steps.add(RosaryStep(RosaryPrayer.OUR_FATHER, "Our Father", null, null))
    repeat(3) { i ->
        steps.add(RosaryStep(RosaryPrayer.HAIL_MARY, "Hail Mary ${i + 1}/3", null, null))
    }
    steps.add(RosaryStep(RosaryPrayer.GLORY_BE, "Glory Be", null, null))
    set.mysteries.forEachIndexed { decade, mystery ->
        steps.add(RosaryStep(RosaryPrayer.OUR_FATHER, "$mystery: Our Father", decade, -1))
        repeat(10) { bead ->
            steps.add(RosaryStep(RosaryPrayer.HAIL_MARY, "$mystery: Hail Mary ${bead + 1}/10", decade, bead))
        }
        steps.add(RosaryStep(RosaryPrayer.GLORY_BE, "$mystery: Glory Be", decade, -1))
    }
    steps.add(RosaryStep(RosaryPrayer.HAIL_HOLY_QUEEN, "Hail Holy Queen", null, null))
    steps.add(RosaryStep(RosaryPrayer.SIGN_OF_CROSS, "Sign of the Cross", null, null))
    return steps
}
