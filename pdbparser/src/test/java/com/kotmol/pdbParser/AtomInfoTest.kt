/*
 *  Copyright 2020 James Andreas
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


package com.kotmol.pdbParser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class AtomInfoTest {

    @Test
    @DisplayName("touch test the atomInfo setup")
    fun touchTestOfAtomInfo() {
        val info = AtomInformationTable.atomTable

        assertEquals("hydrogen", info[1 - 1].name)
        assertEquals("H", info[1 - 1].symbol)
        assertEquals(120, info[1 - 1].vanDerWaalsRadius)

        assertEquals("carbon", info[6 - 1].name)
        assertEquals("C", info[6 - 1].symbol)
        assertEquals(170, info[6 - 1].vanDerWaalsRadius)

        assertEquals("radium", info[88 - 1].name)
        assertEquals("RA", info[88 - 1].symbol)
        assertEquals(283, info[88 - 1].vanDerWaalsRadius)

    }

    @Test
    @DisplayName("scan eleHash hash set for nulls and bad behavior")
    fun scanHashSetForNulls() {
        val hashSet = AtomInformationTable.atomSymboltoAtomNumNameColor

        for (item in hashSet) {
            val foo = item.key
            val yup = hashSet.containsKey(foo)
            assertTrue(yup)
            val molNum = item.value
            assertNotNull(molNum)
            assertNotEquals(0, molNum)
        }
    }

}
