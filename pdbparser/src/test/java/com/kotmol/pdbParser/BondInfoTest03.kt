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

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "deprecation",
        "UNUSED_ANONYMOUS_PARAMETER",
        "UNUSED_EXPRESSION",
        "MemberVisibilityCanBePrivate",
        "FunctionWithLambdaExpressionBody",
        "UnusedMainParameter", "JoinDeclarationAndAssignment",
        "CanBePrimaryConstructorProperty", "RemoveEmptyClassBody",
        "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNUSED_VALUE",
        "ConstantConditionIf", "ReplaceSingleLineLet",
        "ReplaceJavaStaticMethodWithKotlinAnalog",
        "NestedLambdaShadowedImplicitParameter"
)

package com.kotmol.pdbParser

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class BondInfoTest03 {

/*    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        println("Setup")
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        println("Teardown")
    }*/

    @Test
    @DisplayName("touch all hash entries")
    fun testAllHashEntries() {
        val bondLookup = BondInfo()

        for (item in bondLookup.kotmolBondLookup) {
            //println("number of entries in item: ${item.value}")

            val foo = item.value
            assertNotEquals(0, foo)
        }


    }

    @Test
    @DisplayName("check badList for orphaned pair")
    fun testBadList() {
        val foundMatch = BooleanArray(badList.size)
        val allTrue = BooleanArray(badList.size) { true }
        //for (item in badList) {
        for (i in (badList.indices)) {

            val a1 = badList[i].atom_1
            val a2 = badList[i].atom_2
            for (j in (badList.indices)) {
                if (j == i)
                    continue
                val b1 = badList[j].atom_1
                val b2 = badList[j].atom_2
                if (a1 == b1
                        || a1 == b2
                        || a2 == b1
                        || a2 == b2) {
                    foundMatch[i] = true
                    foundMatch[j] = true
                    continue // one atom matches at least
                }
            }
        }
        for (k in (foundMatch.indices)) {
            if (!foundMatch[k]) {
                val bad = badList[k]
                println("Found (expected) orphaned entry: ${badList[k]}")
            }
        }
    }

    /*
     * a Test List that containes one orphaned pair of bond atoms (NX and NY)
     */
    companion object {
        val badList = listOf(
                BondInfo.KotmolBondRecord(
                        aromatic = false,
                        atom_1 = "C1'",
                        atom_2 = "H1'",
                        bond_order = 1f,
                        bond_type = "sing",
                        length = 1.095f
                ),
                BondInfo.KotmolBondRecord(
                        aromatic = false,
                        atom_1 = "NX",
                        atom_2 = "NY",
                        bond_order = 1f,
                        bond_type = "sing",
                        length = 1.434f
                ),
                BondInfo.KotmolBondRecord(
                        aromatic = false,
                        atom_1 = "C1'",
                        atom_2 = "N3",
                        bond_order = 1f,
                        bond_type = "sing",
                        length = 1.402f
                ))
    }

//    @Test
//    @DisplayName( "this should fail")
//    fun testThisFail() {
//        assertEquals(1,2 )
//    }
}
