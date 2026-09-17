package com.sanctum.core.feature.fasting.domain

import kotlinx.serialization.Serializable

val ROOT_VEGETABLES = setOf("potato", "onion", "garlic", "carrot", "radish", "beetroot", "ginger")

@Serializable
data class JainRecipe(
    val id: String,
    val title: String,
    val ingredients: List<String> = emptyList(),
)

fun JainRecipe.isCompliant(): Boolean =
    ingredients.none { it.lowercase() in ROOT_VEGETABLES }

fun filterCompliant(recipes: List<JainRecipe>): List<JainRecipe> = recipes.filter { it.isCompliant() }
