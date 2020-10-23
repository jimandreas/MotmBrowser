package com.bammellab.motm.data

import com.bammellab.motm.data.Corpus.motmDateByKey
import com.bammellab.motm.data.Corpus.motmImageList
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.fail
import java.lang.NumberFormatException


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

        for (iter in motmImageList.withIndex()) {
            val listSize = motmImageList.size
            val entry = iter.value
            val num = entry.substringBefore('-')
            when (motmImageList.size - iter.index) {
                243 -> assertEquals("242", num)
                242 -> assertEquals("243", num)

                204 -> assertEquals("203", num)
                203 -> assertEquals("204", num)
                else -> {
                    try {
                        Integer.parseInt(num)
                    } catch (e: NumberFormatException) {
                        fail("bad value $num for integer at index ${motmImageList.size - iter.index}")
                    }

                    assertEquals(num.toInt(), motmImageList.size - iter.index)
                }
            }
        }
    }
}