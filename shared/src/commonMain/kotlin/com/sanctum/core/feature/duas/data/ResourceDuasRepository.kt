package com.sanctum.core.feature.duas.data

import com.sanctum.core.feature.duas.presentation.Dua
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sanctum.shared.generated.resources.Res

@Serializable
private data class DuaJson(
    val id: String,
    val title: String,
    val original_text: String,
    val translated_text: String,
    val transliteration: String? = null,
)

class ResourceDuasRepository : DuasRepository {
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getDuas(religionId: String): List<Dua> {
        return try {
            val bytes = Res.readBytes("files/$religionId/duas.json")
            val text = bytes.decodeToString()
            val duasJson = json.decodeFromString<List<DuaJson>>(text)
            duasJson.map {
                Dua(
                    id = it.id,
                    title = it.title,
                    originalText = it.original_text,
                    translation = it.translated_text,
                    transliteration = it.transliteration,
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
