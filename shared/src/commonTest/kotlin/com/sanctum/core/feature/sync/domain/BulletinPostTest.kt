package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BulletinPostTest {

    @Test
    fun testIsActiveWhenNotExpired() {
        val post = BulletinPost(
            title = "Test Post",
            body = "This is a test post.",
            venue = "Test Venue",
            expiresAt = 1000L,
        )
        assertTrue(post.isActive(999L), "Post should be active before expiration")
    }

    @Test
    fun testIsActiveWhenExpired() {
        val post = BulletinPost(
            title = "Test Post",
            body = "This is a test post.",
            venue = "Test Venue",
            expiresAt = 1000L,
        )
        assertFalse(post.isActive(1001L), "Post should not be active after expiration")
    }

    @Test
    fun testIsActiveExactlyAtExpiration() {
        val post = BulletinPost(
            title = "Test Post",
            body = "This is a test post.",
            venue = "Test Venue",
            expiresAt = 1000L,
        )
        assertFalse(post.isActive(1000L), "Post should not be active exactly at expiration")
    }

    @Test
    fun testValidationFailsOnBlankTitle() {
        assertFailsWith<IllegalArgumentException> {
            BulletinPost(title = "   ", body = "Body", venue = "Venue", expiresAt = 1000L)
        }
    }

    @Test
    fun testValidationFailsOnBlankBody() {
        assertFailsWith<IllegalArgumentException> {
            BulletinPost(title = "Title", body = "", venue = "Venue", expiresAt = 1000L)
        }
    }

    @Test
    fun testValidationFailsOnBlankVenue() {
        assertFailsWith<IllegalArgumentException> {
            BulletinPost(title = "Title", body = "Body", venue = "   ", expiresAt = 1000L)
        }
    }

    @Test
    fun testValidationFailsOnNegativeExpiration() {
        assertFailsWith<IllegalArgumentException> {
            BulletinPost(title = "Title", body = "Body", venue = "Venue", expiresAt = -1L)
        }
    }
}
