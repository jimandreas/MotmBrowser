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

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class BondInfoTest04 {
    /*
     * for every bond list, scan the list and verify that all bond pairs have at least one
     * match of an atom to another atom.   That is to say - in the directed graph
     * that is described by the bond pairs - there is no orphaned pair orbiting alone.
     */
    @Test
    @DisplayName("check all lists for orphaned bond pairs")
    fun checkAllListsForOrphanedParis() {
        val bondLookup = BondInfo()

        for (item in bondLookup.kotmolBondLookup) {
            scanListForOphanedPairs(item.value, item.key)
        }
    }

    /*
     * the actual scan function - the "foundMatch" boolean array should go completely true
     * as the connections are tested.
     */
    fun scanListForOphanedPairs(testList: List<BondInfo.KotmolBondRecord>, residueName: String) {
        val foundMatch = BooleanArray(testList.size)
        val allTrue = BooleanArray(testList.size) { true }
        //for (item in badList) {
        for (i in (testList.indices)) {

            val a1 = testList[i].atom_1
            val a2 = testList[i].atom_2
            for (j in (testList.indices)) {
                if (j == i)
                    continue
                val b1 = testList[j].atom_1
                val b2 = testList[j].atom_2
                if (a1 == b1
                        || a1 == b2
                        || a2 == b1
                        || a2 == b2) {
                    // minimally one atom matches between the two pairs
                    foundMatch[i] = true
                    foundMatch[j] = true
                    continue
                }
            }
        }
        for (k in (foundMatch.indices)) {
            if (!foundMatch[k]) {
                val bad = testList[k]
                println("$residueName: Found orphaned entry: ${testList[k]}")
            }
        }
        assertArrayEquals(allTrue, foundMatch)
    }
}
