package com.sanctum.core.feature.places.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MosqueFinderTest {

    @Test
    fun `builds search url`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=mosque+Cairo&format=json&limit=10",
            buildMosqueSearchUrl("Cairo"),
        )
    }

    @Test
    fun `parses mosques and skips invalid`() {
        val mosques = parseMosques(
            """[{"display_name":"Al-Azhar, Cairo","lat":"30.0","lon":"31.2"},{"lat":"bad","lon":"1"}]""",
        )
        assertEquals(1, mosques.size)
        assertEquals("Al-Azhar", mosques[0].name)
        assertTrue(parseMosques("[]").isEmpty())
    }
}
