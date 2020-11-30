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

@file:Suppress("UNUSED_VARIABLE")

package com.bammellab.motm.data

import com.bammellab.motm.data.Corpus.motmDateByKey
import com.bammellab.motm.data.Corpus.motmThumbnailImageList
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail


/**
 * boundary testing of basic date string functionq
 */
class CorpusTest {
    @Test
    @DisplayName("test motm key to date function")
    fun testMonthMap() {
        assertThat(1, `is`(1))
        assertThat(motmDateByKey(1), `is`("January 2000"))
        assertThat(motmDateByKey(203), `is`("November 2016"))
        assertThat(motmDateByKey(0), `is`("INVALID DATE"))
        // .put(203, "November 2016")

    }

    /**
     * testing the completeness and order of the motmImageList
     *   This test takes advantage of the tagging the name
     *   of the image with the Motm index number.
     *
     *   Note that on two occasions a pair of strings are swapped in position.
     */
    @Test
    @DisplayName("check the contents of the motmImageList")
    fun motmImageListGet() {

        for (iter in motmThumbnailImageList.withIndex()) {
            val listSize = motmThumbnailImageList.size
            val entry = iter.value
            val num = entry.substringBefore('-')
            when (iter.index+1) {
                243 -> assertEquals("242", num)
                242 -> assertEquals("243", num)

                204 -> assertEquals("203", num)
                203 -> assertEquals("204", num)
                else -> {
                    try {
                        Integer.parseInt(num)
                    } catch (e: NumberFormatException) {
                        fail("bad value $num for integer at index ${motmThumbnailImageList.size - iter.index}")
                    }

                    assertEquals(num.toInt(), iter.index+1)
                }
            }
        }
    }
}