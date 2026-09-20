package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class FamilyCalendar(
    val id: String,
    val memberFlavorIds: Set<String>,
    val events: List<SharedEvent>,
) {
    init {
        require(id.isNotBlank()) { "Calendar ID must not be blank" }
        require(memberFlavorIds.isNotEmpty()) { "A family calendar must have at least one member flavor" }
        require(memberFlavorIds.all { it.isNotBlank() }) { "Member flavor IDs must not be blank" }
    }

    fun hasMember(flavorId: String): Boolean {
        return memberFlavorIds.contains(flavorId)
    }

    @Serializable
    data class SharedEvent(
        val title: String,
        val atMs: Long,
    ) {
        init {
            require(title.isNotBlank()) { "Shared event title must not be blank" }
        }
    }
}
