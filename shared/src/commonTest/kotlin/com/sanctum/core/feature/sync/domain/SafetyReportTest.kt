package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SafetyReportTest {

    @Test
    fun `creates valid safety report`() {
        val report = SafetyReport(
            reporterId = "user123",
            zoneId = "zone456",
            category = "INTOLERANCE",
            details = "Some details",
        )
        assertEquals("user123", report.reporterId)
        assertEquals("zone456", report.zoneId)
        assertEquals("INTOLERANCE", report.category)
        assertEquals("Some details", report.details)
    }

    @Test
    fun `fails when reporterId is blank`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            SafetyReport(reporterId = "   ", zoneId = "z", category = "INTOLERANCE", details = "d")
        }
        assertEquals("reporterId cannot be blank", ex.message)
    }

    @Test
    fun `fails when zoneId is blank`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            SafetyReport(reporterId = "r", zoneId = "", category = "INTOLERANCE", details = "d")
        }
        assertEquals("zoneId cannot be blank", ex.message)
    }

    @Test
    fun `fails when details are blank`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            SafetyReport(reporterId = "r", zoneId = "z", category = "INTOLERANCE", details = "  \n ")
        }
        assertEquals("details cannot be blank", ex.message)
    }

    @Test
    fun `fails on invalid category`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            SafetyReport(reporterId = "r", zoneId = "z", category = "INVALID_CAT", details = "d")
        }
        assertEquals("Invalid category: INVALID_CAT", ex.message)
    }
}
