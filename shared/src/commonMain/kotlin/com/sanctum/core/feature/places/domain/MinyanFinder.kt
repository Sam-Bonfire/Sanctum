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

const val MINYAN_QUORUM = 10

@Serializable
data class Minyan(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val committedCount: Int = 0,
)

fun Minyan.hasQuorum(): Boolean = committedCount >= MINYAN_QUORUM

fun buildMinyanSearchUrl(city: String): String {
    val query = "synagogue ${city.trim()}".trim().replace(" ", "+")
    return "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
}

fun parseMinyans(jsonText: String): List<Minyan> {
    val json = Json { ignoreUnknownKeys = true }
    val arr = json.parseToJsonElement(jsonText) as? JsonArray ?: return emptyList()
    return arr.mapNotNull { element ->
        val obj = element as? JsonObject ?: return@mapNotNull null
        val lat = obj["lat"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        val lon = obj["lon"]?.jsonPrimitive?.doubleOrNull ?: return@mapNotNull null
        Minyan(
            name = obj["display_name"]?.jsonPrimitive?.content?.substringBefore(",") ?: "Minyan",
            latitude = lat,
            longitude = lon,
        )
    }
}

class MinyanFinder {
    private val client = HttpClient()

    suspend fun nearby(city: String): Result<List<Minyan>> {
        return try {
            val response = client.get(buildMinyanSearchUrl(city))
            Result.success(parseMinyans(response.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
