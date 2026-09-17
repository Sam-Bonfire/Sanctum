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
data class Mosque(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

fun buildMosqueSearchUrl(city: String): String {
    val query = "mosque ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

fun parseMosques(jsonText: String): List<Mosque> {
    val json = Json { ignoreUnknownKeys = true }
    val arr = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
    return arr.mapNotNull { element ->
        val obj = element as? JsonObject ?: return@mapNotNull null
        val lat = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val lon = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        Mosque(
            name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Mosque",
            latitude = lat,
            longitude = lon,
        )
    }
}

class MosqueFinder {
    private val client = HttpClient()

    suspend fun nearby(city: String): Result<List<Mosque>> {
        return try {
            val response = client.get(buildMosqueSearchUrl(city))
            Result.success(parseMosques(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
