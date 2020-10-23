package com.bammellab.motm.data

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class MotmImageDownloadTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    @DisplayName("test getFirstTiffImageURL lookup function")
    fun testGetFirstTiffImageURL() {

        // test not present / error condition
        var match = MotmImageDownload.getFirstTiffImageURL(-1)
        assertEquals("", match)

        // test end condition
        match = MotmImageDownload.getFirstTiffImageURL(0)
        assertEquals("", match)

        // first entry
        match = MotmImageDownload.getFirstTiffImageURL(250)
        assertNotEquals("", match)

        // last entry
        match = MotmImageDownload.getFirstTiffImageURL(1)
        assertNotEquals("", match)
        assertEquals("https://cdn.rcsb.org/pdb101/motm/tiff/1-Myoglobin-geis-myoglobin.tif",
                match)
    }
}