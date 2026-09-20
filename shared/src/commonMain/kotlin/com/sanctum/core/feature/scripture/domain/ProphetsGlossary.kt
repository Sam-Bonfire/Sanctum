package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

class ProphetsGlossary {

    @Serializable
    data class Entry(
        val term: String,
        val summary: String,
        val traditions: List<String>,
    )

    val entries: List<Entry> = listOf(
        Entry(
            term = "Abraham",
            summary = "Patriarch recognized in Judaism, Christianity, and Islam.",
            traditions = listOf("Judaism", "Christianity", "Islam"),
        ),
        Entry(
            term = "Moses",
            summary = "Prophet who led the Israelites out of Egypt.",
            traditions = listOf("Judaism", "Christianity", "Islam"),
        ),
        Entry(
            term = "Jesus",
            summary = "Central figure of Christianity, revered as a prophet in Islam.",
            traditions = listOf("Christianity", "Islam"),
        ),
    )

    fun search(query: String): List<Entry> {
        val lowerQuery: String = query.lowercase()
        return entries.filter { entry: Entry ->
            entry.term.lowercase().contains(lowerQuery) || entry.summary.lowercase().contains(lowerQuery)
        }
    }
}
