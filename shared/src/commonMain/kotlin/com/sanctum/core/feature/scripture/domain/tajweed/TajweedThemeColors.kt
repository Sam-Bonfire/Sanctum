package com.sanctum.core.feature.scripture.domain.tajweed

import androidx.compose.ui.graphics.Color

data class TajweedColors(
    val ghunnah: Color,
    val ikhfa: Color,
    val idgham: Color,
    val qalqalah: Color,
    val madd: Color,
    val silent: Color,
)

object TajweedThemeColors {
    val Light = TajweedColors(
        ghunnah = Color(0xFFD81B60),
        ikhfa = Color(0xFF00897B),
        idgham = Color(0xFF43A047),
        qalqalah = Color(0xFF1E88E5),
        madd = Color(0xFFE53935),
        silent = Color(0xFF757575),
    )

    val Dark = TajweedColors(
        ghunnah = Color(0xFFF06292),
        ikhfa = Color(0xFF4DB6AC),
        idgham = Color(0xFF81C784),
        qalqalah = Color(0xFF64B5F6),
        madd = Color(0xFFE57373),
        silent = Color(0xFF9E9E9E),
    )
}
