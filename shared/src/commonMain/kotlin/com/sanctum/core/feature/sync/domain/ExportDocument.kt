package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class ExportSection(
    val heading: String,
    val body: String,
)

@Serializable
data class ExportDocument(
    val title: String,
    val sections: List<ExportSection> = emptyList(),
)

fun ExportDocument.isEmpty(): Boolean = sections.all { it.heading.isBlank() && it.body.isBlank() }

fun ExportDocument.renderText(): String = buildString {
    appendLine(title.trim())
    appendLine("=".repeat(title.trim().length.coerceAtLeast(1)))
    for (section in sections) {
        appendLine()
        if (section.heading.isNotBlank()) {
            appendLine(section.heading.trim())
            appendLine("-".repeat(section.heading.trim().length))
        }
        if (section.body.isNotBlank()) appendLine(section.body.trim())
    }
}.trimEnd() + "\n"
