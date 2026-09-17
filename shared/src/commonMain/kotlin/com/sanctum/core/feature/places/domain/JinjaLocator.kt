package com.sanctum.core.feature.places.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class JinjaSpot(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val etiquette: String = "",
)

fun buildJinjaSearchUrl(city: String): String {
    val query = "shinto shrine ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

fun jinjaEtiquette(): String =
    "Bow before the torii gate, purify hands and mouth at the chozuya, " +
        "offer coins quietly, bow twice, clap twice, bow once more."

fun parseJinjaSpots(jsonText: String): List<JinjaSpot> {
    val json = Json { ignoreUnknownKeys = true }
    val arr = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
    return arr.mapNotNull { element ->
        val obj = element as? JsonObject ?: return@mapNotNull null
        val lat = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val lon = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        JinjaSpot(
            name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Jinja",
            latitude = lat,
            longitude = lon,
            etiquette = jinjaEtiquette(),
        )
    }
}

class JinjaLocator {
    private val client = HttpClient()

    suspend fun nearby(city: String): Result<List<JinjaSpot>> {
        return try {
            val response = client.get(buildJinjaSearchUrl(city))
            Result.success(parseJinjaSpots(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
