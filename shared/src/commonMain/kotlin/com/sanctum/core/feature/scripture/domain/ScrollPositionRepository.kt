package com.sanctum.core.feature.scripture.domain

interface ScrollPositionRepository {
    fun getScrollIndex(chapterId: String): Int
    fun getScrollOffset(chapterId: String): Int
    fun saveScrollPosition(chapterId: String, index: Int, offset: Int)
}
