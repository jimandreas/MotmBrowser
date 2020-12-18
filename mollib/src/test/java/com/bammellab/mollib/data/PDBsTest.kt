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

import com.bammellab.mollib.data.PDBs.obtainPdbMappingList
import com.bammellab.mollib.data.PDBs.pullTheMap
import com.bammellab.mollib.data.PdbInfo.obtainPdbInfoList
import com.bammellab.mollib.data.PdbInfo.searchPdbInfoForNameMatch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PDBsTest {

    private var mapFromMotmNUmberToPdbList:  Map<Int, List<String>> = mapOf(Pair(0, listOf("")))

    @BeforeEach
    fun setUp() {
        mapFromMotmNUmberToPdbList = pullTheMap()
        assertNotNull(mapFromMotmNUmberToPdbList)
    }

    /**
     * a basic test to insure that the map of motm numbers to pdb code lists is initialized
     */

    @Test
    @DisplayName("Basic test of Grouped List structure and extent")
    fun pullTheMapTest() {
        val firstList = mapFromMotmNUmberToPdbList[1]
        val lastList = mapFromMotmNUmberToPdbList[252] // current as of Dec 2020

        assertNotNull(firstList)
        assertNotNull(lastList)
        assert(firstList!!.isNotEmpty())
        assert(lastList!!.isNotEmpty())

        assertEquals(9, lastList.size)

    }

    /**
     * verify that for all PDB codes in each map entry that there is a pdbInfoList entry
     *
     * This verifies the consistency of the two lists in one direction
     */

    @Test
    @DisplayName("Match all entryies in Grouped List with an entry in the pdbInfoList")
    fun matchPdbGroupedListWithPdbInfoList() {
        for (item in mapFromMotmNUmberToPdbList) {
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

    @Test
    @DisplayName("All entrys in pdbInfOList have a MotmNumberToPDB entry")
    fun matchPdbInfoListToMotmMapList() {

        val pdbInfo = obtainPdbInfoList()
        val pdbMappingInfo = obtainPdbMappingList()

        for (item in pdbInfo) {
            val lookupVal = pdbMappingInfo.filter { it.pdbName.equals(item.pdbName, ignoreCase = true) }
            if (lookupVal.isEmpty()) {
                println("no match for ${item.pdbName} in PDBs.kt")
            }
            assertFalse(lookupVal.isEmpty())

        }
    }
}