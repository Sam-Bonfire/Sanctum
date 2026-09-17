package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthSessionTest {

    @Test
    fun `tracks sign in state`() {
        val session = AuthSession("u1", "a@b.com", token = "t")
        assertTrue(session.isSignedIn())
        assertFalse(session.signOut().isSignedIn())
        assertEquals("", session.signOut().userId)
    }

    @Test
    fun `validates emails`() {
        assertTrue(isValidEmail("user@example.com"))
        assertFalse(isValidEmail("not-an-email"))
        assertFalse(isValidEmail("a@b"))
    }
}
