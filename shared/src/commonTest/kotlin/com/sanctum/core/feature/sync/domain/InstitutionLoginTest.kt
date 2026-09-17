package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InstitutionLoginTest {

    @Test
    fun `validates invite login`() {
        assertTrue(InstitutionLogin("parish-1", "ABC123", "admin").isValid())
        assertFalse(InstitutionLogin("", "ABC123").isValid())
        assertFalse(InstitutionLogin("parish-1", "short").isValid())
        assertFalse(InstitutionLogin("parish-1", "ABC123", "owner").isValid())
    }

    @Test
    fun `detects staff roles`() {
        assertTrue(InstitutionLogin("m1", "ABC123", "clergy").isStaff())
        assertFalse(InstitutionLogin("m1", "ABC123", "member").isStaff())
    }
}
