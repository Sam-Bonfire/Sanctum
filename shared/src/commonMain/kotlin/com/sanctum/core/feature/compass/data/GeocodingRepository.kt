package com.sanctum.core.feature.compass.data

import com.sanctum.core.feature.compass.domain.GeoLocation
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class GeocodingRepository {
    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun geocode(city: String): Result<Pair<GeoLocation, String>> {
        return try {
            val response = client.get("https://nominatim.openstreetmap.org/search?q=$city&format=json&limit=1")
            val jsonArr = json.parseToJsonElement(response.bodyAsText()) as JsonArray
            if (jsonArr.isEmpty()) {
                Result.failure(Exception("Location not found"))
            } else {
                val obj = jsonArr[0] as JsonObject
                val lat = obj["lat"]?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0
                val lon = obj["lon"]?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0
                val name = obj["name"]?.jsonPrimitive?.content ?: city
                Result.success(Pair(GeoLocation(lat, lon), name))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
