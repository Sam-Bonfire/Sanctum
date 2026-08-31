package com.sanctum.core.feature.scripture.presentation

import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScriptureVerse

sealed class ScriptureReaderItem {
    data class Header(val chapter: ScriptureChapter) : ScriptureReaderItem()
    data class Verse(val chapter: ScriptureChapter, val verse: ScriptureVerse) : ScriptureReaderItem()
}
