/*
 *  Copyright 2021 Bammellab / James Andreas
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
    // MAINTENANCE - update last MotM number as they are added
    private val lastMotmIdentifier = 253 // current as of Jan 2021
    private val lastMotmPdbListSize = 1 // number of pdb entries for Jan 2021

    private var mapFromMotmNUmberToPdbList:  Map<Int, List<String>> = mapOf(Pair(0, listOf("")))

    /**
     * All of the tests use this map
     *    where a MotM int maps to a list of PDB strings
     */
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
        val lastList = mapFromMotmNUmberToPdbList[lastMotmIdentifier]

        assertNotNull(firstList)
        assertNotNull(lastList)
        assertTrue(firstList!!.isNotEmpty())
        assertTrue(lastList!!.isNotEmpty())

        assertEquals(lastMotmPdbListSize, lastList.size)

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
                    println("no match found for $pdb at $item in PdbInfoArray/pdbInfoList, did you add it?")
                }
                // returned search value should be same as search pdb
                assertEquals(pdb.lowercase(), resultList[0].pdbName.lowercase())
                if (resultList.size > 1) {
                    println("Error: duplicate pdb in pdbInfoList: $pdb, $resultList")
                }
                assertEquals(1, resultList.size)
                assertEquals(pdb.lowercase(), resultList[0].pdbName.lowercase())
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