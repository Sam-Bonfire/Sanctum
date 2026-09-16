package com.sanctum.core.feature.groups.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StudyGroupTest {

    private val group = StudyGroup("g1", "Seekers", listOf("islam", "christianity"))

    @Test
    fun `detects cross flavor groups`() {
        assertTrue(group.isCrossFlavor())
        assertFalse(group.copy(flavorIds = listOf("islam")).isCrossFlavor())
    }

    @Test
    fun `join adds member`() {
        assertEquals(listOf("u1"), group.join("u1").memberIds)
    }

    @Test
    fun `rejects duplicate and full groups`() {
        assertFailsWith<IllegalArgumentException> { group.join("u1").join("u1") }
        assertFailsWith<IllegalArgumentException> {
            group.copy(memberIds = List(50) { "m$it" }).join("new")
        }
    }
}
