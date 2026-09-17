package com.sanctum.core.feature.reflection.domain

import kotlinx.serialization.Serializable

@Serializable
data class MudraGuide(
    val id: String,
    val name: String,
    val meaning: String,
    val steps: List<String> = emptyList(),
    val imageKey: String = "",
)

fun MudraGuide.isComplete(): Boolean =
    id.isNotBlank() && name.isNotBlank() && steps.isNotEmpty()

fun findMudra(guides: List<MudraGuide>, id: String): MudraGuide? = guides.firstOrNull { it.id == id }
