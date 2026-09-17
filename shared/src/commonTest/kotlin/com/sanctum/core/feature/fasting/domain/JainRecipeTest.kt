package com.sanctum.core.feature.fasting.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JainRecipeTest {

    @Test
    fun `rejects root vegetables`() {
        assertFalse(JainRecipe("r1", "Curry", listOf("Potato", "rice")).isCompliant())
        assertFalse(JainRecipe("r2", "Soup", listOf("lentils", "Onion")).isCompliant())
        assertTrue(JainRecipe("r3", "Fruit", listOf("mango")).isCompliant())
    }

    @Test
    fun `filters compliant recipes`() {
        val recipes = listOf(
            JainRecipe("r1", "A", listOf("garlic")),
            JainRecipe("r2", "B", listOf("rice")),
        )
        assertEquals(listOf("r2"), filterCompliant(recipes).map { it.id })
    }
}
