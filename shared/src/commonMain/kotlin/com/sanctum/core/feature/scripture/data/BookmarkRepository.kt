package com.sanctum.core.feature.scripture.data

import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.BookmarkTag
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<Bookmark>>
    fun getTags(): Flow<List<BookmarkTag>>
    suspend fun createTag(name: String, colorHex: String)
    suspend fun assignTag(verseId: Int, tagId: Int)
    suspend fun unassignTag(verseId: Int, tagId: Int)
    suspend fun renameTag(tagId: Int, newName: String)
    suspend fun deleteTag(tagId: Int)
}
