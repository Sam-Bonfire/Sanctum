package com.sanctum.core.feature.scripture.data

import com.russhwolf.settings.Settings
import com.sanctum.core.feature.scripture.domain.ScrollPositionRepository

class SettingsScrollPositionRepository(
    private val settings: Settings,
) : ScrollPositionRepository {

    override fun getScrollIndex(chapterId: String): Int {
        return settings.getInt(keyIndex(chapterId), 0)
    }

    override fun getScrollOffset(chapterId: String): Int {
        return settings.getInt(keyOffset(chapterId), 0)
    }

    override fun saveScrollPosition(chapterId: String, index: Int, offset: Int) {
        settings.putInt(keyIndex(chapterId), index)
        settings.putInt(keyOffset(chapterId), offset)
    }

    private fun keyIndex(chapterId: String): String = "scroll_index_$chapterId"
    private fun keyOffset(chapterId: String): String = "scroll_offset_$chapterId"
}
