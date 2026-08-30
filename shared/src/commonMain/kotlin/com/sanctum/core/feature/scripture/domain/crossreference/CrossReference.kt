package com.sanctum.core.feature.scripture.domain.crossreference

data class CrossReference(
    val id: String,
    val sourceVerseId: String,
    val targetVerseId: String,
    val targetChapterName: String,
    val targetVerseNumber: Int,
    val previewText: String,
)
