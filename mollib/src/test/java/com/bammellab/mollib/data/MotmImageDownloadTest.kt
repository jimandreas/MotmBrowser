/*
 *  Copyright 2020 Bammellab / James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.bammellab.mollib.data

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
        var match = MotmImageDownload.motmTiffImageName(-1)
        assertEquals("", match)

        // test end condition
        match = MotmImageDownload.motmTiffImageName(0)
        assertEquals("", match)

        // first entry
        match = MotmImageDownload.motmTiffImageName(250)
        assertNotEquals("", match)

        // last entry
        match = MotmImageDownload.motmTiffImageName(1)
        assertNotEquals("", match)
        assertEquals("1-Myoglobin-geis-0218-myoglobin",
                match)
    }

    @Test
    @DisplayName("test basic screensaver list function")
    fun buildMotmImageList() {
        val foo : List<MotmImageDownload.FavoriteMotmImage> = MotmImageDownload.buildMotmImageList()
        assertNotEquals(0, foo.size)

        // right now there are 252 months of the Molecule of the Month.
        //  So there should be 252 entries in the image list
        assertEquals(252, foo.size)
    }
}