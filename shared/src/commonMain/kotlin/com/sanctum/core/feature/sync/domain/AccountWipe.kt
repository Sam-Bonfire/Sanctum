package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class WipeConfirmation(
    val typedPhrase: String,
    val acknowledgedIrreversible: Boolean,
)

const val WIPE_PHRASE = "DELETE"

fun WipeConfirmation.isConfirmed(): Boolean =
    typedPhrase.trim() == WIPE_PHRASE && acknowledgedIrreversible

fun wipeSteps(): List<String> = listOf(
    "bookmarks",
    "notes",
    "highlights",
    "journal",
    "settings",
    "account",
)
