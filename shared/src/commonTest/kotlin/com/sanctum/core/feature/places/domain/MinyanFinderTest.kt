package com.sanctum.core.feature.places.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MinyanFinderTest {

    @Test
    fun `quorum needs ten`() {
        assertFalse(Minyan("m", 0.0, 0.0, 9).hasQuorum())
        assertTrue(Minyan("m", 0.0, 0.0, 10).hasQuorum())
    }

    @Test
    fun `builds search url`() {
        assertEquals(
            "https://nominatim.openstreetmap.org/search?q=synagogue+Brooklyn&format=json&limit=10",
            buildMinyanSearchUrl("Brooklyn"),
        )
    }

    @Test
    fun `parses minyans`() {
        val minyans = parseMinyans("""[{"display_name":"Shul, Brooklyn","lat":"40.6","lon":"-73.9"}]""")
        assertEquals(1, minyans.size)
        assertEquals("Shul", minyans[0].name)
    }
}
