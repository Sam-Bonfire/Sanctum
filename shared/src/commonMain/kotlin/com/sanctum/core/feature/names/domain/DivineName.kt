package com.sanctum.core.feature.names.domain

import kotlinx.serialization.Serializable

@Serializable
data class DivineName(
    val id: Int,
    val arabic: String,
    val transliteration: String,
    val meaning: String,
    val explanation: String,
    val audioFileName: String = "",
)
