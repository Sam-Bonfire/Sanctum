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
data class KosherSpot(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String = "",
)

fun buildKosherSearchUrl(city: String): String {
    val query = "kosher restaurant ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

fun parseKosherSpots(jsonText: String): List<KosherSpot> {
    val json = Json { ignoreUnknownKeys = true }
    val arr = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
    return arr.mapNotNull { element ->
        val obj = element as? JsonObject ?: return@mapNotNull null
        val lat = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val lon = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        KosherSpot(
            name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Kosher spot",
            latitude = lat,
            longitude = lon,
            category = obj["type"]?.jsonPrimitive?.content ?: "",
        )
    }
}

class KosherLocator {
    private val client = HttpClient()

    suspend fun nearby(city: String): Result<List<KosherSpot>> {
        return try {
            val response = client.get(buildKosherSearchUrl(city))
            Result.success(parseKosherSpots(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
