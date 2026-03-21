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

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class BondInfoTest02 {

/*    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        println("Setup")
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        println("Teardown")
    }*/

    @Test
    @DisplayName( "check that all entries exist")
    fun testAllHashEntriesExist() {
        val bondLookup = BondInfo()

        val theList = listOf(
        "ala",
        "arg",
        "asn",
        "asp",
        "asx",
        "cys",
        "glu",
        "gln",
        "glx",
        "gly",
        "his",
        "ile",
        "leu",
        "lys",
        "met",
        "phe",
        "pro",
        "ser",
        "thr",
        "trp",
        "tyr",
        "val")
        for (l in theList) {
            val theBondInfo = bondLookup.kotmolBondLookup[l]
            assertNotEquals(theBondInfo, null)
            val theBondInfoResult = theBondInfo!![0]

        }

    }

//    @Test
//    @DisplayName( "this should fail")
//    fun testThisFail() {
//        assertEquals(1,2 )
//    }
}