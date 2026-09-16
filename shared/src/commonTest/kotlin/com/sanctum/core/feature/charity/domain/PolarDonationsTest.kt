package com.sanctum.core.feature.charity.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class PolarDonationsTest {

    private val config = PolarDonationConfig("https://donate.polar.sh/testorg")

    @Test
    fun `returns base url when no params`() {
        assertEquals("https://donate.polar.sh/testorg", buildPolarCheckoutUrl(config))
    }

    @Test
    fun `appends amount category and reference`() {
        val url = buildPolarCheckoutUrl(config, 10.0, CharityCategory.SADAQAH, "abc")
        assertEquals(
            "https://donate.polar.sh/testorg?amount=1000&category=sadaqah&reference=abc",
            url,
        )
    }

    @Test
    fun `returns empty for blank base`() {
        assertEquals("", buildPolarCheckoutUrl(PolarDonationConfig("  ")))
    }
}
