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

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

// https://stackoverflow.com/a/54938582 - on copying a data class array

internal class BondInfoTest05 {
    /*
     *  verify that that map/copy operation creates a clone of a list/data class
     *    and that there is no reuse of the objects, i.e. pollution of the original
     *    data class array.
     */
    @Test
    @DisplayName("check that original record that was copied has bondRecordCreated still false")
    fun checkForCleanCopyOfBondList() {
        val exampleBondList = BondInfo().ala

        val theCopy = ArrayList(exampleBondList.map { it.copy() })

        exampleBondList[0].bondRecordCreated = true

        val isFlagFalse = theCopy[0].bondRecordCreated

        assertFalse(isFlagFalse)
        assertTrue(exampleBondList[0].bondRecordCreated)
        assertFalse(exampleBondList[0] === theCopy[0])
        assertFalse(exampleBondList[0] == theCopy[0])
        assertTrue(exampleBondList[1] == theCopy[1])
    }


}
