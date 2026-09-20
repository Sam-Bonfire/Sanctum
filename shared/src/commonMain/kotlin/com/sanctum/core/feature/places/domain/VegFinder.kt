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

class VegFinder {

    @Serializable
    data class VegSpot(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val category: String = "",
    )

    companion object {
        fun buildVegSearchUrl(city: String): String {
            val query: String = "vegetarian vegan restaurant ${city.trim()}".trim().replace(" ", "+")
            return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
        }

        fun parseVegSpots(jsonText: String): List<VegSpot> {
            val json: Json = Json { ignoreUnknownKeys = true }
            val arr: JsonArray = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
            return arr.mapNotNull { element ->
                val obj: JsonObject = element as? JsonObject ?: return@mapNotNull null
                val lat: Double = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
                val lon: Double = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
                VegSpot(
                    name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Veg spot",
                    latitude = lat,
                    longitude = lon,
                    category = obj["type"]?.jsonPrimitive?.content ?: "",
                )
            }
        }
    }

    private val client: HttpClient = HttpClient()

    suspend fun nearby(city: String): Result<List<VegSpot>> {
        return try {
            val response: io.ktor.client.statement.HttpResponse = client.get(buildVegSearchUrl(city))
            Result.success(parseVegSpots(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
