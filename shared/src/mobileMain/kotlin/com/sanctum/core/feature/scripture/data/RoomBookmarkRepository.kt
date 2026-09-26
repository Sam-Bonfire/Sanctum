package com.sanctum.core.feature.scripture.data

import com.sanctum.core.core.database.BookmarkTagCrossRef
import com.sanctum.core.core.database.BookmarkTagEntity
import com.sanctum.core.core.database.UserDataDao
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.BookmarkTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomBookmarkRepository(
    private val userDataDao: UserDataDao,
) : BookmarkRepository {
    override fun getBookmarks(): Flow<List<Bookmark>> =
        userDataDao.getBookmarksWithTags().map { list ->
            list.map { withTags ->
                Bookmark(
                    id = withTags.bookmark.id,
                    verseId = withTags.bookmark.verseId,
                    timestampMs = withTags.bookmark.timestampMs,
                    tags = withTags.tags.map { BookmarkTag(it.id, it.name, it.colorHex) },
                )
            }
        }

    override fun getTags(): Flow<List<BookmarkTag>> =
        userDataDao.getAllTags().map { list ->
            list.map { BookmarkTag(it.id, it.name, it.colorHex) }
        }

    override suspend fun createTag(name: String, colorHex: String) {
        userDataDao.insertTag(BookmarkTagEntity(name = name, colorHex = colorHex))
    }

    override suspend fun assignTag(verseId: Int, tagId: Int) {
        userDataDao.assignTag(BookmarkTagCrossRef(verseId = verseId, tagId = tagId))
    }

    override suspend fun unassignTag(verseId: Int, tagId: Int) {
        userDataDao.unassignTag(verseId = verseId, tagId = tagId)
    }

    override suspend fun renameTag(tagId: Int, newName: String) {
        userDataDao.renameTag(tagId = tagId, name = newName)
    }

    override suspend fun deleteTag(tagId: Int) {
        userDataDao.deleteTag(tagId = tagId)
    }
}
