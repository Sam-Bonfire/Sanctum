package com.sanctum.core.feature.places.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChurchFinderTest {

    @Test
    fun `builds search url from city`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=church+parish+Brooklyn&format=json&limit=10",
            buildChurchSearchUrl("Brooklyn"),
        )
    }

    @Test
    fun `parses spots and skips invalid entries`() {
        val spots: List<ChurchFinder.ChurchSpot> = parseChurchSpots(
            """[{"display_name":"St. Patrick's, Brooklyn, NY","lat":"40.6","lon":"-73.9","type":"place_of_worship"},{"lat":"bad","lon":"1"}]""",
        )
        assertEquals(1, spots.size)
        assertEquals("St. Patrick's", spots[0].name)
        assertEquals(40.6, spots[0].latitude)
        assertEquals("place_of_worship", spots[0].category)
    }

    @Test
    fun `returns empty for invalid json`() {
        assertTrue(parseChurchSpots("{}").isEmpty())
    }
}
