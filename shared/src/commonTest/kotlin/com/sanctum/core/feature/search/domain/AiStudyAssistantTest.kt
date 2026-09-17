package com.sanctum.core.feature.search.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AiStudyAssistantTest {

    @Test
    fun `gates on premium and endpoint`() {
        val config = AiStudyConfig("https://ai.example.com", "model")
        assertTrue(canUseAiAssistant(true, config))
        assertFalse(canUseAiAssistant(false, config))
        assertFalse(canUseAiAssistant(true, AiStudyConfig()))
    }

    @Test
    fun `builds prompt with context`() {
        assertEquals(
            "[islam] What is zakat?\nContext: verse 2:110",
            buildAiStudyPrompt(AiStudyRequest("What is zakat?", "verse 2:110", "islam")),
        )
    }

    @Test
    fun `rejects blank questions`() {
        assertFailsWith<IllegalArgumentException> { buildAiStudyPrompt(AiStudyRequest("  ")) }
    }
}
