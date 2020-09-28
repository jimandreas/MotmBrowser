/*
 * Copyright (C) 2016-2018 James Andreas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")
package com.bammellab.mollib.protein

import com.bammellab.mollib.common.math.Vector3

class PdbAtom {

    var atomNumber: Int = 0
    var atomType: Int = 0
    var atomBondCount: Int = 0
    lateinit var atomName: String
    lateinit var residueName: String
    var chainId: Char = ' '
    var residueSeqNumber: Int = 0
    var residueInsertionCode: Char = ' '  //  Code for insertion of residues.
    lateinit var atomPosition: Vector3
    lateinit var elementSymbol: String

    /**
     * debugging of picking
     */
    val pickedAtom = false

    companion object {
        const val IS_ATOM = 1
        const val IS_HETATM = 2
        const val IS_NUCLEIC = 3
    }
}

/*
ATOM line per PDB spec V33
COLUMNS DATA TYPE FIELD DEFINITION
-------------------------------------------------------------------------------------
1 - 6 Record name "ATOM "
7 - 11 Integer serial Atom serial number.
13 - 16 Atom name Atom name.
17 Character altLoc Alternate location indicator.
18 - 20 Residue name resName Residue name.
22 Character chainID Chain identifier.
23 - 26 Integer resSeq Residue sequence number.
27 AChar iCode Code for insertion of residues.
31 - 38 Real(8.3) x Orthogonal coordinates for X in Angstroms.
39 - 46 Real(8.3) y Orthogonal coordinates for Y in Angstroms.
47 - 54 Real(8.3) z Orthogonal coordinates for Z in Angstroms.
55 - 60 Real(6.2) occupancy Occupancy.
61 - 66 Real(6.2) tempFactor Temperature factor.
77 - 78 LString(2) element Element symbol, right-justified.
79 - 80 LString(2) charge Charge on the atom.
 */
