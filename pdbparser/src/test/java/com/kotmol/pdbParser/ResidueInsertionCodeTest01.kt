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

internal class ResidueInsertionCodeTest01 {

    /**
     * @link https://proteopedia.org/wiki/index.php/Unusual_sequence_numbering
     * 1IGY - residue 82 is distingushed by the insertion codes
     */
    lateinit var str : ByteArrayInputStream
    lateinit var anAtom : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 1bna.pdb
        val numbering = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        anAtom = """
ATOM   2856  HE2 HIS B  81      -5.928 -18.845 -19.529  1.00 15.00           H  
ATOM   2857  N   LEU B  82      -8.102 -23.106 -16.211  1.00 54.46           N  
ATOM   2858  CA  LEU B  82      -9.303 -22.824 -15.441  1.00 54.46           C  
ATOM   2859  C   LEU B  82      -9.912 -21.576 -16.058  1.00 54.46           C  
ATOM   2860  O   LEU B  82      -9.269 -20.528 -16.091  1.00 20.39           O  
ATOM   2861  CB  LEU B  82      -8.941 -22.562 -13.968  1.00 20.39           C  
ATOM   2862  CG  LEU B  82      -8.054 -23.612 -13.283  1.00 20.39           C  
ATOM   2863  CD1 LEU B  82      -6.847 -22.911 -12.724  1.00 20.39           C  
ATOM   2864  CD2 LEU B  82      -8.799 -24.398 -12.197  1.00 20.39           C  
ATOM   2865  H   LEU B  82      -7.232 -23.058 -15.763  1.00 15.00           H  
ATOM   2866  N   SER B  82A    -11.132 -21.699 -16.574  1.00 62.30           N  
ATOM   2867  CA  SER B  82A    -11.791 -20.565 -17.197  1.00 62.30           C  
ATOM   2868  C   SER B  82A    -12.893 -19.950 -16.337  1.00 62.30           C  
ATOM   2869  O   SER B  82A    -12.630 -19.034 -15.567  1.00 94.33           O  
ATOM   2870  CB  SER B  82A    -12.312 -20.938 -18.582  1.00 94.33           C  
ATOM   2871  OG  SER B  82A    -12.673 -19.777 -19.303  1.00 94.33           O  
ATOM   2872  H   SER B  82A    -11.591 -22.564 -16.533  1.00 15.00           H  
ATOM   2873  HG  SER B  82A    -13.361 -19.305 -18.827  1.00 15.00           H  
ATOM   2874  N   SER B  82B    -14.121 -20.438 -16.464  1.00 34.63           N  
ATOM   2875  CA  SER B  82B    -15.223 -19.894 -15.676  1.00 34.63           C  
ATOM   2876  C   SER B  82B    -15.034 -20.433 -14.273  1.00 34.63           C  
ATOM   2877  O   SER B  82B    -15.427 -21.559 -13.972  1.00 40.86           O  
ATOM   2878  CB  SER B  82B    -16.555 -20.371 -16.231  1.00 40.86           C  
ATOM   2879  OG  SER B  82B    -16.620 -21.784 -16.157  1.00 40.86           O  
ATOM   2880  H   SER B  82B    -14.289 -21.171 -17.093  1.00 15.00           H  
ATOM   2881  HG  SER B  82B    -15.905 -22.165 -16.672  1.00 15.00           H  
ATOM   2882  N   LEU B  82C    -14.401 -19.641 -13.419  1.00 42.01           N  
ATOM   2883  CA  LEU B  82C    -14.141 -20.088 -12.059  1.00 42.01           C  
ATOM   2884  C   LEU B  82C    -15.240 -19.853 -11.036  1.00 42.01           C  
ATOM   2885  O   LEU B  82C    -15.495 -18.731 -10.594  1.00 23.41           O  
ATOM   2886  CB  LEU B  82C    -12.783 -19.564 -11.585  1.00 23.41           C  
ATOM   2887  CG  LEU B  82C    -11.648 -20.325 -12.293  1.00 23.41           C  
ATOM   2888  CD1 LEU B  82C    -10.291 -19.676 -12.115  1.00 23.41           C  
ATOM   2889  CD2 LEU B  82C    -11.628 -21.757 -11.785  1.00 23.41           C  
ATOM   2890  H   LEU B  82C    -14.107 -18.751 -13.705  1.00 15.00           H  
ATOM   2891  N   THR B  83     -15.949 -20.929 -10.732  1.00 24.92           N
        """.trimIndent()

        str = anAtom.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    @Test
    @DisplayName( "test the handling of Residue Insertion Codes")
    fun testResidueInsertionCodeHandling() {

        val mol = Molecule()
        var messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        assertEquals(2891, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(36, atoms.size)

        val firstAtomNumber = mol.atomNumberList[0]
        val firstAtom = mol.atomNumberToAtomInfoHash[firstAtomNumber]
        assertNotNull(firstAtom)
        assertEquals("HE2", firstAtom!!.atomName)


    }
}