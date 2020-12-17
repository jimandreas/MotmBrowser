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

import com.bammellab.mollib.data.PDBs.groupedList
import com.bammellab.mollib.data.PDBs.setupGroupedList
import com.bammellab.mollib.data.PdbInfo.searchPdbInfo
import com.bammellab.mollib.data.PdbInfo.searchPdbInfoForNameMatch
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import timber.log.Timber

internal class PDBsTest {

    private var thePdbGroupedList:  Map<Int, List<String>> = mapOf(Pair(0, listOf("")))

    @BeforeEach
    fun setUp() {
        setupGroupedList()
        thePdbGroupedList = groupedList
        assertNotNull(thePdbGroupedList)
    }

    @Test
    @DisplayName("Basic test of Grouped List structure and extent")
    fun pullTheMapTest() {
        val firstList = thePdbGroupedList[1]
        val lastList = thePdbGroupedList[252] // current as of Dec 2020

        assertNotNull(firstList)
        assertNotNull(lastList)
        assert(firstList!!.isNotEmpty())
        assert(lastList!!.isNotEmpty())

        assertEquals(9, lastList.size)

    }

    @Test
    @DisplayName("Match all entryies in Grouped List with an entry in the pdbInfoList")
    fun matchPdbGroupedListWithPdbInfoList() {
        for (item in thePdbGroupedList) {
            for (pdb in item.value) {
                val resultList = searchPdbInfoForNameMatch(pdb)
                assertNotNull(resultList)
                if (resultList.isEmpty()) {
                    println("no match found for $pdb, failing")
                }
                // returned search value should be same as search pdb
                assertEquals(pdb.toLowerCase(), resultList[0].pdbName.toLowerCase())
                if (resultList.size > 1) {
                    println("Error: duplicate pdb in pdbInfoList: $pdb, $resultList")
                }
                assertEquals(1, resultList.size)

                assertEquals(pdb.toLowerCase(), resultList[0].pdbName.toLowerCase())
            }

        }
    }
}