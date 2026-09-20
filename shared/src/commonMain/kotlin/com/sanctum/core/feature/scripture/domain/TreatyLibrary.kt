package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

public class TreatyLibrary {

    @Serializable
    public data class Treaty(
        val id: String,
        val title: String,
        val year: Int,
        val summary: String,
    )

    private val treaties: MutableList<Treaty> = mutableListOf()

    public fun addTreaty(treaty: Treaty) {
        treaties.add(treaty)
    }

    public fun getAllTreaties(): List<Treaty> {
        return treaties.toList()
    }

    public fun searchTreaties(query: String): List<Treaty> {
        if (query.isBlank()) return emptyList()
        val lowerQuery: String = query.lowercase()
        return treaties.filter {
            it.title.lowercase().contains(lowerQuery) || it.summary.lowercase().contains(lowerQuery)
        }
    }

    public fun getTreatyById(id: String): Treaty? {
        return treaties.find { it.id == id }
    }
}
