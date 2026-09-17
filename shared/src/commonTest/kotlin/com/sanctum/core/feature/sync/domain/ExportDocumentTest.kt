package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExportDocumentTest {

    @Test
    fun `renders printable text`() {
        val doc = ExportDocument(
            "Journal",
            listOf(ExportSection("Morning", "Grateful."), ExportSection("", "Untitled note.")),
        )
        val text = doc.renderText()
        assertTrue(text.startsWith("Journal\n======="))
        assertTrue(text.contains("Morning\n-------"))
        assertTrue(text.contains("Untitled note."))
        assertFalse(doc.isEmpty())
    }

    @Test
    fun `detects empty documents`() {
        assertTrue(ExportDocument("T", listOf(ExportSection("", ""))).isEmpty())
        assertTrue(ExportDocument("T").isEmpty())
        assertEquals("T\n=\n", ExportDocument("T").renderText())
    }
}
