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
        ghunnah = Color(0xFFD81B60), // Deeper pink for contrast
        ikhfa = Color(0xFF00897B),   // Darker teal
        idgham = Color(0xFF43A047),  // Darker green
        qalqalah = Color(0xFF1E88E5),// Darker blue
        madd = Color(0xFFE53935),    // Deeper red
        silent = Color(0xFF757575)   // Darker gray
    )

    val Dark = TajweedColors(
        ghunnah = Color(0xFFF06292), // Lighter pinkish
        ikhfa = Color(0xFF4DB6AC),   // Teal
        idgham = Color(0xFF81C784),  // Green
        qalqalah = Color(0xFF64B5F6),// Blue
        madd = Color(0xFFE57373),    // Reddish
        silent = Color(0xFF9E9E9E)   // Gray
    )
}
