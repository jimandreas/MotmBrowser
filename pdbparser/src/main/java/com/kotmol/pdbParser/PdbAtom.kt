/*
 *  Copyright 2021 James Andreas
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

@file:Suppress("unused")

package com.kotmol.pdbParser

import kotlin.math.sqrt

class PdbAtom {
    var atomNumber = 0
    var atomType = AtomType.IS_NOT_A_TYPE
    var atomName: String = ""
    var residueName: String = ""
    var chainId: Char = ' '
    var residueSeqNumber = 0
    var residueInsertionCode: Char = ' '
    lateinit var atomPosition: KotmolVector3
    var elementSymbol: String = ""
    var atomBondCount = 0
    var renderThisAtomAsSphere = false

    enum class AtomType {
        IS_ATOM,
        IS_HETATM,
        IS_NUCLEIC,
        IS_TER_RECORD,
        IS_NOT_A_TYPE
    }
}

class Helix {
    var chainId: Char = ' '
    lateinit var atom: PdbAtom
}

class KotmolVector3(
        var x: Float = 0.0f,
        var y: Float = 0.0f,
        var z: Float = 0.0f) {

    fun distanceTo(q: KotmolVector3): Float {
        val a = x - q.x
        val b = y - q.y
        val c = z - q.z
        return sqrt(a * a + b * b + c * c)

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
