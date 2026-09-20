package com.sanctum.core.feature.places.domain

import com.sanctum.core.feature.places.domain.GurdwaraFinder.Companion.parseGurdwaraSpots
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GurdwaraFinderTest {

    @Test
    fun `builds search url from city`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=gurdwara+Amritsar&format=json&limit=10",
            buildGurdwaraSearchUrl("Amritsar"),
        )
    }

    @Test
    fun `parses spots and skips invalid entries`() {
        val spots = parseGurdwaraSpots(
            """[{"display_name":"Golden Temple, Amritsar, Punjab","lat":"31.62","lon":"74.87","type":"place_of_worship"},{"lat":"bad","lon":"1"}]""",
        )
        assertEquals(1, spots.size)
        assertEquals("Golden Temple", spots[0].name)
        assertEquals(31.62, spots[0].latitude)
        assertEquals(74.87, spots[0].longitude)
        assertEquals("place_of_worship", spots[0].category)
    }

    @Test
    fun `returns empty for invalid json`() {
        assertTrue(parseGurdwaraSpots("{}").isEmpty())
    }
}
