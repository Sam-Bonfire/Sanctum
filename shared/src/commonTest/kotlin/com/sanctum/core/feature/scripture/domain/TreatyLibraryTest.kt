package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

public class TreatyLibraryTest {

    @Test
    public fun testAddAndRetrieveTreaties() {
        val library: TreatyLibrary = TreatyLibrary()
        val treaty: TreatyLibrary.Treaty = TreatyLibrary.Treaty("1", "Magna Carta", 1215, "A royal charter of rights.")

        library.addTreaty(treaty)

        val allTreaties: List<TreatyLibrary.Treaty> = library.getAllTreaties()
        assertEquals(1, allTreaties.size)
        assertEquals(treaty, allTreaties[0])
    }

    @Test
    public fun testSearchTreaties() {
        val library: TreatyLibrary = TreatyLibrary()
        library.addTreaty(TreatyLibrary.Treaty("1", "Treaty of Westphalia", 1648, "Ended the Thirty Years' War."))
        library.addTreaty(TreatyLibrary.Treaty("2", "Treaty of Versailles", 1919, "Ended WWI."))
        library.addTreaty(TreatyLibrary.Treaty("3", "Charter of Medina", 622, "Interfaith constitution."))

        val searchResult1: List<TreatyLibrary.Treaty> = library.searchTreaties("westphalia")
        assertEquals(1, searchResult1.size)
        assertEquals("Treaty of Westphalia", searchResult1[0].title)

        val searchResult2: List<TreatyLibrary.Treaty> = library.searchTreaties("Ended")
        assertEquals(2, searchResult2.size)

        val searchResult3: List<TreatyLibrary.Treaty> = library.searchTreaties("Nothing")
        assertTrue(searchResult3.isEmpty())

        val searchResult4: List<TreatyLibrary.Treaty> = library.searchTreaties(" ")
        assertTrue(searchResult4.isEmpty())
    }

    @Test
    public fun testGetTreatyById() {
        val library: TreatyLibrary = TreatyLibrary()
        val treaty1: TreatyLibrary.Treaty = TreatyLibrary.Treaty("1", "Treaty One", 2000, "Summary One")
        val treaty2: TreatyLibrary.Treaty = TreatyLibrary.Treaty("2", "Treaty Two", 2010, "Summary Two")

        library.addTreaty(treaty1)
        library.addTreaty(treaty2)

        val foundTreaty: TreatyLibrary.Treaty? = library.getTreatyById("2")
        assertNotNull(foundTreaty)
        assertEquals("Treaty Two", foundTreaty.title)

        val missingTreaty: TreatyLibrary.Treaty? = library.getTreatyById("3")
        assertNull(missingTreaty)
    }
}
