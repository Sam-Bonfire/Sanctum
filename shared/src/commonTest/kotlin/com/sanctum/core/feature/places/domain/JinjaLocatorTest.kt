package com.sanctum.core.feature.places.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JinjaLocatorTest {

    @Test
    fun `builds search url`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=shinto+shrine+Kyoto&format=json&limit=10",
            buildJinjaSearchUrl("Kyoto"),
        )
    }

    @Test
    fun `parses spots with etiquette`() {
        val spots = parseJinjaSpots("""[{"display_name":"Fushimi Inari, Kyoto","lat":"34.9","lon":"135.7"}]""")
        assertEquals(1, spots.size)
        assertEquals("Fushimi Inari", spots[0].name)
        assertTrue(spots[0].etiquette.contains("Bow"))
    }
}
