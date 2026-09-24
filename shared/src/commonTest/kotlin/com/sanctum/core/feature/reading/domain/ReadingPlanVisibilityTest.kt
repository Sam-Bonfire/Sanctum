package com.sanctum.core.feature.reading.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ReadingPlanVisibilityTest {

    private fun plan(
        id: String,
        category: PlanCategory,
    ) = ReadingPlan(id, id, id, category, 30, 1, emptyList())

    private val all =
        listOf(
            plan("quran_khatam_30", PlanCategory.QURAN),
            plan("bible_in_a_year", PlanCategory.BIBLE),
            plan("nt_in_a_year", PlanCategory.GOSPEL),
            plan("psalms_30", PlanCategory.PSALMS),
            plan("genesis_30", PlanCategory.FOUNDATIONS),
            plan("proverbs_31", PlanCategory.WISDOM),
        )

    @Test
    fun islamSeesKhatamAndGenericButNotBible() {
        assertEquals(
            setOf("quran_khatam_30", "psalms_30", "genesis_30", "proverbs_31"),
            all.visibleFor("islam").map { it.id }.toSet(),
        )
    }

    @Test
    fun christianitySeesBibleAndGenericButNotKhatam() {
        assertEquals(
            setOf("bible_in_a_year", "nt_in_a_year", "psalms_30", "genesis_30", "proverbs_31"),
            all.visibleFor("christianity").map { it.id }.toSet(),
        )
    }

    @Test
    fun otherFlavorsSeeOnlyGeneric() {
        for (flavor in listOf("hinduism", "buddhism", "jewish", "sikhism", "jainism", "shinto", "taoism")) {
            assertEquals(
                setOf("psalms_30", "genesis_30", "proverbs_31"),
                all.visibleFor(flavor).map { it.id }.toSet(),
                "flavor $flavor",
            )
        }
    }
}
