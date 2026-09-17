package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompareViewTest {

    private val sahih = TranslationPane("sahih", "en", "In the name of God")
    private val pickthall = TranslationPane("pickthall", "en", "In the name of Allah")

    @Test
    fun `adds panes up to max then rolls`() {
        val view = CompareView(1).addPane(sahih).addPane(pickthall)
        assertEquals(2, view.panes.size)
        val rolled = view.addPane(TranslationPane("yusuf", "en", "Text"))
        assertEquals(listOf("pickthall", "yusuf"), rolled.panes.map { it.translationId })
    }

    @Test
    fun `ignores duplicates and removes`() {
        val view = CompareView(1).addPane(sahih).addPane(sahih)
        assertEquals(1, view.panes.size)
        assertTrue(view.removePane("sahih").panes.isEmpty())
    }
}
