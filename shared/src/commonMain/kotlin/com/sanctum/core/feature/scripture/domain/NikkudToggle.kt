package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class NikkudSetting(
    val showVowels: Boolean = true,
)

private val NIKKUD_RANGE = '\u0591'..'\u05C7'

fun stripNikkud(text: String): String = text.filter { it !in NIKKUD_RANGE }

fun applyNikkudSetting(text: String, setting: NikkudSetting): String =
    if (setting.showVowels) text else stripNikkud(text)
