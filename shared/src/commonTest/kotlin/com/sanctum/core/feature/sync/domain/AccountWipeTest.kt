package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountWipeTest {

    @Test
    fun `confirms only with phrase and acknowledgement`() {
        assertTrue(WipeConfirmation("DELETE", true).isConfirmed())
        assertFalse(WipeConfirmation("delete", true).isConfirmed())
        assertFalse(WipeConfirmation("DELETE", false).isConfirmed())
    }

    @Test
    fun `covers all data stores`() {
        assertEquals(
            listOf("bookmarks", "notes", "highlights", "journal", "settings", "account"),
            wipeSteps(),
        )
    }
}
