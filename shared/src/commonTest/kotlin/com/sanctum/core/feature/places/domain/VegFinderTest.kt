package com.sanctum.core.feature.places.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VegFinderTest {

    @Test
    fun `builds search url from city`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=vegetarian+vegan+restaurant+Brooklyn&format=json&limit=10",
            VegFinder.buildVegSearchUrl("Brooklyn"),
        )
    }

    @Test
    fun `parses spots and skips invalid entries`() {
        val spots: List<VegFinder.VegSpot> = VegFinder.parseVegSpots(
            """[{"display_name":"Cafe, Brooklyn, NY","lat":"40.6","lon":"-73.9","type":"restaurant"},{"lat":"bad","lon":"1"}]""",
        )
        assertEquals(1, spots.size)
        assertEquals("Cafe", spots[0].name)
        assertEquals(40.6, spots[0].latitude)
    }

    @Test
    fun `returns empty for invalid json`() {
        assertTrue(VegFinder.parseVegSpots("{}").isEmpty())
    }
}
