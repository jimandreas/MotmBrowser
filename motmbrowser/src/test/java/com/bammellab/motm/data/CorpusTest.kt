package com.bammellab.motm.data

import com.bammellab.motm.data.Corpus.motmDateByKey
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test




/**
 * boundary testing of basic date string functionq
 */
class CorpusTest {
    @Test
    fun testMonthMap() {
        assertThat(1, `is`(1))
        assertThat(motmDateByKey(1), `is`("January 2000"))
        assertThat(motmDateByKey(203), `is`("November 2016"))
        assertThat(motmDateByKey(0), `is`("INVALID DATE"))
        // .put(203, "November 2016")

    }
}