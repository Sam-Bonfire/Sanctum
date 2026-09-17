package com.sanctum.core.core.analytics

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdConfigTest {

    private val config = AdConfig(true, "ca-app-pub-123")

    @Test
    fun `shows ads only for free tier with config`() {
        assertTrue(shouldShowAds(false, config))
        assertFalse(shouldShowAds(true, config))
        assertFalse(shouldShowAds(false, AdConfig(false, "ca-app-pub-123")))
        assertFalse(shouldShowAds(false, AdConfig(true, "")))
    }
}
