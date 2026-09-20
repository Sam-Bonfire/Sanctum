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

fun buildGurdwaraSearchUrl(city: String): String {
    val query = "gurdwara ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

class GurdwaraFinder {
    @Serializable
    data class GurdwaraSpot(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val category: String = "",
    )

    private val client = HttpClient()

    suspend fun nearby(city: String): Result<List<GurdwaraSpot>> {
        return try {
            val response = client.get(buildGurdwaraSearchUrl(city))
            Result.success(parseGurdwaraSpots(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        fun parseGurdwaraSpots(jsonText: String): List<GurdwaraSpot> {
            val json = Json { ignoreUnknownKeys = true }
            val arr = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
            return arr.mapNotNull { element ->
                val obj = element as? JsonObject ?: return@mapNotNull null
                val lat = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
                val lon = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
                GurdwaraSpot(
                    name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Gurdwara",
                    latitude = lat,
                    longitude = lon,
                    category = obj["type"]?.jsonPrimitive?.content ?: "",
                )
            }
        }
    }
}
