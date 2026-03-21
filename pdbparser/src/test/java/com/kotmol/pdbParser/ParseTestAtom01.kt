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
import java.io.ByteArrayInputStream

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class ParseTestAtom01 {

    lateinit var str : ByteArrayInputStream
    lateinit var anAtom : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 1bna.pdb
        anAtom = """
            ATOM      1  O5'  DC A   1      18.935  34.195  25.617  1.00 64.35           O 
        """.trimIndent()

        str = anAtom.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    @Test
    @DisplayName( "test an ATOM line")
    fun testThis() {
        val line = str.readBytes()
        val str = String(line)

        // println(str)
        assertEquals(anAtom, str)
    }
}