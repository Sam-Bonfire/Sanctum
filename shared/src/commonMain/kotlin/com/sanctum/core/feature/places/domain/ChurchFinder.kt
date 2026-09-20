package com.sanctum.core.feature.places.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive

fun buildChurchSearchUrl(city: String): String {
    val query: String = "church parish ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

fun parseChurchSpots(jsonText: String): List<ChurchFinder.ChurchSpot> {
    val json: Json = Json { ignoreUnknownKeys = true }
    val arr: JsonArray = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
    return arr.mapNotNull { element: JsonElement ->
        val obj: JsonObject = element as? JsonObject ?: return@mapNotNull null
        val lat: Double = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val lon: Double = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val nameStr: String = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Church"
        val catStr: String = obj["type"]?.jsonPrimitive?.content ?: ""
        ChurchFinder.ChurchSpot(
            name = nameStr,
            latitude = lat,
            longitude = lon,
            category = catStr,
        )
    }
}

class ChurchFinder {
    @Serializable
    data class ChurchSpot(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val category: String = "",
    )

    private val client: HttpClient = HttpClient()

    suspend fun nearby(city: String): Result<List<ChurchSpot>> {
        return try {
            val response: HttpResponse = client.get(buildChurchSearchUrl(city))
            Result.success(parseChurchSpots(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
