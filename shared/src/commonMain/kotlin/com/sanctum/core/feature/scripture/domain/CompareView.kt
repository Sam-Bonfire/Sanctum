package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class TranslationPane(
    val translationId: String,
    val language: String,
    val text: String,
)

@Serializable
data class CompareView(
    val verseId: Int,
    val panes: List<TranslationPane> = emptyList(),
    val maxPanes: Int = 2,
)

fun CompareView.addPane(pane: TranslationPane): CompareView {
    if (panes.any { it.translationId == pane.translationId }) return this
    if (panes.size >= maxPanes) return copy(panes = panes.drop(1) + pane)
    return copy(panes = panes + pane)
}

fun CompareView.removePane(translationId: String): CompareView =
    copy(panes = panes.filter { it.translationId != translationId })
