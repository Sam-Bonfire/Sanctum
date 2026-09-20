package com.sanctum.core.core.design

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class A11yLabelsTest {

    @Test
    fun testGetLabelsWithExistingScreenId() {
        val registry: Map<String, List<String>> = mapOf(
            "home" to listOf("Home Screen", "Main navigation view"),
            "settings" to listOf("Settings Menu"),
        )
        val a11yLabels: A11yLabels = A11yLabels(registry = registry)

        val result: List<String> = a11yLabels.getLabels("home")
        val expected: List<String> = listOf("Home Screen", "Main navigation view")

        assertEquals(expected, result)
    }

    @Test
    fun testGetLabelsWithMissingScreenId() {
        val registry: Map<String, List<String>> = mapOf(
            "home" to listOf("Home Screen"),
        )
        val a11yLabels: A11yLabels = A11yLabels(registry = registry)

        val fallback: List<String> = listOf("Fallback Label")
        val result: List<String> = a11yLabels.getLabels("settings", fallback)

        assertEquals(fallback, result)
    }

    @Test
    fun testIsCompleteReturnsTrueWhenAllScreensPresent() {
        val registry: Map<String, List<String>> = mapOf(
            "home" to listOf("Home Screen"),
            "settings" to listOf("Settings Menu"),
        )
        val a11yLabels: A11yLabels = A11yLabels(registry = registry)

        val expectedScreens: Set<String> = setOf("home", "settings")
        val result: Boolean = a11yLabels.isComplete(expectedScreens)

        assertTrue(result)
    }

    @Test
    fun testIsCompleteReturnsFalseWhenScreenMissing() {
        val registry: Map<String, List<String>> = mapOf(
            "home" to listOf("Home Screen"),
        )
        val a11yLabels: A11yLabels = A11yLabels(registry = registry)

        val expectedScreens: Set<String> = setOf("home", "settings")
        val result: Boolean = a11yLabels.isComplete(expectedScreens)

        assertFalse(result)
    }
}
