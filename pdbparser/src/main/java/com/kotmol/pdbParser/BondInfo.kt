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

        "MemberVisibilityCanBePrivate"
)

/*@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "deprecation",
        "UNUSED_ANONYMOUS_PARAMETER",
        "UNUSED_EXPRESSION",
        "MemberVisibilityCanBePrivate",
        "FunctionWithLambdaExpressionBody",
        "UnusedMainParameter",
        "JoinDeclarationAndAssignment",
        "CanBePrimaryConstructorProperty",
        "RemoveEmptyClassBody",
        "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE",
        "UNUSED_VALUE",
        "ConstantConditionIf",
        "ReplaceSingleLineLet",
        "ReplaceJavaStaticMethodWithKotlinAnalog",
        "NestedLambdaShadowedImplicitParameter",
        "ArrayInDataClass" // suppresses complains about overriding equals and hashCode() operations
)*/

package com.kotmol.pdbParser

/**
 * bond information
 *   as extracted from the database at:
 *   @link: https://www.ebi.ac.uk/pdbe/api/pdb/compound/bonds/
 */

/**
 *  NOTES:
 *  from the 1992 PDB file format specification:
 *
 * PROTEIN DATA BANK
 * ATOMIC COORDINATE AND BIBLIOGRAPHIC ENTRY FORMAT DESCRIPTION
 * February 1992
 *
 *  Atom names employed for polynucleotides generally follow the precedents set for mononucleotides.
 * The following points are worthy of note.
 * (i) The prime character (') commonly used to denote atoms of the ribose originally was
 * avoided because of non-uniformity of its external representation. An asterisk (*)
 * therefore was used in its place, in entries released through January 1992.
 * (ii) Of the four characters reserved for atom names the leftmost two are reserved for the
 * chemical symbol (right justified) and the remaining two denote the atom's position.
 * (iii) Atoms exocyclic to the ring systems have the same position identifier as the atom
 * to which they are bonded except if this would result in identical atom names. In this
 * case an alphabetic character is used to avoid ambiguity.
 * (iv) The ring-oxygen atom of the ribose is denoted O4 rather than O1.
 * (v) The extra oxygen atom at the free 5' phosphate terminus is designated OXT. This
 * atom will be placed first in the coordinate set.
 */

class BondInfo {
    data class KotmolBondRecord(
            val atom_1: String,
            val atom_2: String,
            val bond_order: Float,
            val bond_type: String,
            val aromatic: Boolean = false,
            val length: Float,
            var bondRecordCreated: Boolean = false
    )

    val ala = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.505f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB1", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.468f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.968f
            )
    )
    val arg = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.224f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.36f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.518f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.536f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.096f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.537f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.097f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.098f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD3", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "NE", bond_order = 1f, bond_type = "sing", length = 1.444f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.527f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.097f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.097f),
            KotmolBondRecord(atom_1 = "CZ", atom_2 = "NH1", bond_order = 1f, bond_type = "sing", length = 1.391f),
            KotmolBondRecord(atom_1 = "CZ", atom_2 = "NH2", bond_order = 2f, bond_type = "doub", length = 1.391f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.462f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 0.997f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 0.996f),
            KotmolBondRecord(atom_1 = "NE", atom_2 = "CZ", bond_order = 1f, bond_type = "sing", length = 1.406f),
            KotmolBondRecord(atom_1 = "NE", atom_2 = "HE", bond_order = 1f, bond_type = "sing", length = 1.027f),
            KotmolBondRecord(atom_1 = "NH1", atom_2 = "HH11", bond_order = 1f, bond_type = "sing", length = 1.018f),
            KotmolBondRecord(atom_1 = "NH1", atom_2 = "HH12", bond_order = 1f, bond_type = "sing", length = 1.016f),
            KotmolBondRecord(atom_1 = "NH2", atom_2 = "HH21", bond_order = 1f, bond_type = "sing", length = 1.017f),
            KotmolBondRecord(atom_1 = "NH2", atom_2 = "HH22", bond_order = 1f, bond_type = "sing", length = 1.017f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.981f
            )
    )
    val asn = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.531f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "ND2", bond_order = 1f, bond_type = "sing", length = 1.348f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "OD1", bond_order = 2f, bond_type = "doub", length = 1.213f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.468f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "ND2", atom_2 = "HD21", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "ND2", atom_2 = "HD22", bond_order = 1f, bond_type = "sing", length = 0.97f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val asp = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.209f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.508f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "OD1", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "OD2", bond_order = 1f, bond_type = "sing", length = 1.341f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "OD2", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 0.966f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val asx = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB1", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "XD1", bond_order = 2f, bond_type = "doub", length = 1.642f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "XD2", bond_order = 1f, bond_type = "sing", length = 1.642f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val cys = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.528f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "SG", bond_order = 1f, bond_type = "sing", length = 1.814f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.01f),
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "SG", atom_2 = "HG", bond_order = 1f, bond_type = "sing", length = 1.344f
            )
    )
    val gln = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.528f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "NE2", bond_order = 1f, bond_type = "sing", length = 1.347f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "OE1", bond_order = 2f, bond_type = "doub", length = 1.212f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "NE2", atom_2 = "HE21", bond_order = 1f, bond_type = "sing", length = 0.969f),
            KotmolBondRecord(atom_1 = "NE2", atom_2 = "HE22", bond_order = 1f, bond_type = "sing", length = 0.97f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val glu = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.508f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.531f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "OE1", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "OE2", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.508f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "OE2", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 0.966f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val glx = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.531f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB1", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "XE1", bond_order = 2f, bond_type = "doub", length = 1.642f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "XE2", bond_order = 1f, bond_type = "sing", length = 1.642f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG1", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "HXT", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f
            )
    )
    val gly = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.47f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val his = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.227f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.355f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.522f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.534f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.096f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.51f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.099f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.098f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.072f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "NE2", bond_order = 1.5f, bond_type = "sing", length = 1.374f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "HE1", bond_order = 1f, bond_type = "sing", length = 1.078f),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "NE2", bond_order = 1.5f, bond_type = "sing", length = 1.337f, aromatic = true),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD2", bond_order = 1.5f, bond_type = "doub", length = 1.338f, aromatic = true),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "ND1", bond_order = 1.5f, bond_type = "sing", length = 1.351f, aromatic = true),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.441f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.006f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.007f),
            KotmolBondRecord(atom_1 = "ND1", atom_2 = "CE1", bond_order = 1.5f, bond_type = "doub", length = 1.337f, aromatic = true),
            KotmolBondRecord(atom_1 = "ND1", atom_2 = "HD1", bond_order = 1f, bond_type = "sing", length = 1.016f),
            KotmolBondRecord(atom_1 = "NE2", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 1.016f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.98f
            )
    )
    val ile = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG1", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG2", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD11", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD12", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD13", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "CD1", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "HG12", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "HG13", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG21", bond_order = 1f, bond_type = "sing", length = 1.088f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG22", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG23", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.007f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )

    val leu = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD11", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD12", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD13", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD21", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD22", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD23", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD1", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD2", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.966f
            )
    )
    val lys = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.531f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "CE", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "HE3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "NZ", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.531f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "NZ", atom_2 = "HZ1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "NZ", atom_2 = "HZ2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "NZ", atom_2 = "HZ3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val met = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.343f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.528f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "HE1", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CE", atom_2 = "HE3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "SD", bond_order = 1f, bond_type = "sing", length = 1.814f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.01f),
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "SD", atom_2 = "CE", bond_order = 1f, bond_type = "sing", length = 1.814f
            )
    )
    val phe = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.341f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.505f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "CE1", bond_order = 1.5f, bond_type = "sing", length = 1.382f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD1", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "CE2", bond_order = 1.5f, bond_type = "doub", length = 1.382f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "CZ", bond_order = 1.5f, bond_type = "doub", length = 1.381f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "HE1", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CE2", atom_2 = "CZ", bond_order = 1.5f, bond_type = "sing", length = 1.382f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE2", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 1.081f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD1", bond_order = 1.5f, bond_type = "doub", length = 1.382f, aromatic = true),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD2", bond_order = 1.5f, bond_type = "sing", length = 1.383f, aromatic = true),
            KotmolBondRecord(atom_1 = "CZ", atom_2 = "HZ", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val pro = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.508f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.543f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.543f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CD", atom_2 = "HD3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.544f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "HG3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.486f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CD", bond_order = 1f, bond_type = "sing", length = 1.487f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.966f
            )
    )
    val ser = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "OG", bond_order = 1f, bond_type = "sing", length = 1.428f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "OG", atom_2 = "HG", bond_order = 1f, bond_type = "sing", length = 0.967f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val thr = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG2", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "OG1", bond_order = 1f, bond_type = "sing", length = 1.428f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG21", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG22", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG23", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.007f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "OG1", atom_2 = "HG1", bond_order = 1f, bond_type = "sing", length = 0.967f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val trp = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD1", bond_order = 1f, bond_type = "sing", length = 1.079f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "NE1", bond_order = 1.5f, bond_type = "sing", length = 1.369f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "CE2", bond_order = 1.5f, bond_type = "doub", length = 1.407f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "CE3", bond_order = 1.5f, bond_type = "sing", length = 1.396f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE2", atom_2 = "CZ2", bond_order = 1.5f, bond_type = "sing", length = 1.391f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE3", atom_2 = "CZ3", bond_order = 1.5f, bond_type = "doub", length = 1.366f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE3", atom_2 = "HE3", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD1", bond_order = 1.5f, bond_type = "doub", length = 1.343f, aromatic = true),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD2", bond_order = 1.5f, bond_type = "sing", length = 1.464f, aromatic = true),
            KotmolBondRecord(atom_1 = "CH2", atom_2 = "HH2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CZ2", atom_2 = "CH2", bond_order = 1.5f, bond_type = "doub", length = 1.377f, aromatic = true),
            KotmolBondRecord(atom_1 = "CZ2", atom_2 = "HZ2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CZ3", atom_2 = "CH2", bond_order = 1.5f, bond_type = "sing", length = 1.388f, aromatic = true),
            KotmolBondRecord(atom_1 = "CZ3", atom_2 = "HZ3", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.01f),
            KotmolBondRecord(atom_1 = "NE1", atom_2 = "CE2", bond_order = 1.5f, bond_type = "sing", length = 1.377f, aromatic = true),
            KotmolBondRecord(atom_1 = "NE1", atom_2 = "HE1", bond_order = 1f, bond_type = "sing", length = 0.969f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )
    val tyr = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.207f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.507f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB2", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB3", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "CE1", bond_order = 1.5f, bond_type = "sing", length = 1.381f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD1", atom_2 = "HD1", bond_order = 1f, bond_type = "sing", length = 1.079f),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "CE2", bond_order = 1.5f, bond_type = "doub", length = 1.381f, aromatic = true),
            KotmolBondRecord(atom_1 = "CD2", atom_2 = "HD2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "CZ", bond_order = 1.5f, bond_type = "doub", length = 1.387f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE1", atom_2 = "HE1", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CE2", atom_2 = "CZ", bond_order = 1.5f, bond_type = "sing", length = 1.388f, aromatic = true),
            KotmolBondRecord(atom_1 = "CE2", atom_2 = "HE2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD1", bond_order = 1.5f, bond_type = "doub", length = 1.382f, aromatic = true),
            KotmolBondRecord(atom_1 = "CG", atom_2 = "CD2", bond_order = 1.5f, bond_type = "sing", length = 1.383f, aromatic = true),
            KotmolBondRecord(atom_1 = "CZ", atom_2 = "OH", bond_order = 1f, bond_type = "sing", length = 1.358f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "OH", atom_2 = "HH", bond_order = 1f, bond_type = "sing", length = 0.966f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.968f
            )
    )
    val valine = listOf(
            KotmolBondRecord(atom_1 = "C", atom_2 = "O", bond_order = 2f, bond_type = "doub", length = 1.208f),
            KotmolBondRecord(atom_1 = "C", atom_2 = "OXT", bond_order = 1f, bond_type = "sing", length = 1.342f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "C", bond_order = 1f, bond_type = "sing", length = 1.506f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "CB", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CA", atom_2 = "HA", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG1", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "CG2", bond_order = 1f, bond_type = "sing", length = 1.529f),
            KotmolBondRecord(atom_1 = "CB", atom_2 = "HB", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "HG11", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "HG12", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG1", atom_2 = "HG13", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG21", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG22", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "CG2", atom_2 = "HG23", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "CA", bond_order = 1f, bond_type = "sing", length = 1.469f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H", bond_order = 1f, bond_type = "sing", length = 1.008f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // add H1 and H3 bonds to N
            KotmolBondRecord(atom_1 = "N", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 1.009f),
            KotmolBondRecord(atom_1 = "N", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.009f),
            // end change
            KotmolBondRecord(atom_1 = "OXT", atom_2 = "HXT", bond_order = 1f, bond_type = "sing", length = 0.967f
            )
    )

    val da = listOf(
            /*
             *   !!!
             * These two records were added to capture the bond between the
             * C2' then O2' and HO2'
             * this was borrowed from another bond between an OXT and HXT
             * SEE: 1A1T.pdb
             */
            KotmolBondRecord(atom_1 = "O2'", atom_2 = "HO2'", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "O2'", bond_order = 1f, bond_type = "sing", length = 1.43f),
            /*
             *  End of added record
             */

            KotmolBondRecord(atom_1 = "C1'", atom_2 = "H1'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "N9", bond_order = 1f, bond_type = "sing", length = 1.466f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "H2", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N3", bond_order = 1.5f, bond_type = "doub", length = 1.316f, aromatic = true),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.541f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2'", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2''", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "C2'", bond_order = 1f, bond_type = "sing", length = 1.547f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "H3'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "O3'", bond_order = 1f, bond_type = "sing", length = 1.429f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "C3'", bond_order = 1f, bond_type = "sing", length = 1.549f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "H4'", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "O4'", bond_order = 1f, bond_type = "sing", length = 1.446f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C4", bond_order = 1.5f, bond_type = "doub", length = 1.405f, aromatic = true),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C6", bond_order = 1.5f, bond_type = "sing", length = 1.405f, aromatic = true),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "C4'", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5''", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "N1", bond_order = 1.5f, bond_type = "doub", length = 1.329f, aromatic = true),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "N6", bond_order = 1f, bond_type = "sing", length = 1.384f),
            KotmolBondRecord(atom_1 = "C8", atom_2 = "H8", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "C8", atom_2 = "N7", bond_order = 1.5f, bond_type = "doub", length = 1.301f, aromatic = true),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C2", bond_order = 1.5f, bond_type = "sing", length = 1.319f, aromatic = true),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "C4", bond_order = 1.5f, bond_type = "sing", length = 1.329f, aromatic = true),
            KotmolBondRecord(atom_1 = "N6", atom_2 = "H61", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "N6", atom_2 = "H62", bond_order = 1f, bond_type = "sing", length = 0.971f),
            KotmolBondRecord(atom_1 = "N7", atom_2 = "C5", bond_order = 1.5f, bond_type = "sing", length = 1.355f, aromatic = true),
            KotmolBondRecord(atom_1 = "N9", atom_2 = "C4", bond_order = 1.5f, bond_type = "sing", length = 1.372f, aromatic = true),
            KotmolBondRecord(atom_1 = "N9", atom_2 = "C8", bond_order = 1.5f, bond_type = "sing", length = 1.362f, aromatic = true),
            KotmolBondRecord(atom_1 = "O3'", atom_2 = "HO3'", bond_order = 1f, bond_type = "sing", length = 0.966f),
            KotmolBondRecord(atom_1 = "O4'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.436f),
            KotmolBondRecord(atom_1 = "O5'", atom_2 = "C5'", bond_order = 1f, bond_type = "sing", length = 1.428f),
            KotmolBondRecord(atom_1 = "OP2", atom_2 = "HOP2", bond_order = 1f, bond_type = "sing", length = 0.966f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "HOP3", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "P", bond_order = 1f, bond_type = "sing", length = 1.61f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "O5'", bond_order = 1f, bond_type = "sing", length = 1.609f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP1", bond_order = 2f, bond_type = "doub", length = 1.48f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP2", bond_order = 1f, bond_type = "sing", length = 1.61f
            )
    )
    val dc = listOf(
            /*
             *   !!!
             * These two records were added to capture the bond between the
             * C2' then O2' and HO2'
             * this was borrowed from another bond between an OXT and HXT
             * SEE: 1A1T.pdb
             */
            KotmolBondRecord(atom_1 = "O2'", atom_2 = "HO2'", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "O2'", bond_order = 1f, bond_type = "sing", length = 1.43f),
            /*
             *  End of added record
             */
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "H1'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "N1", bond_order = 1f, bond_type = "sing", length = 1.465f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N3", bond_order = 1f, bond_type = "sing", length = 1.332f),
             /*  BUG should be O2
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2'", bond_order = 2f, bond_type = "doub", length = 1.22f),
              */
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2", bond_order = 2f, bond_type = "doub", length = 1.22f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.541f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2'", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2''", bond_order = 1f, bond_type = "sing", length = 1.091f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "C2'", bond_order = 1f, bond_type = "sing", length = 1.547f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "H3'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "O3'", bond_order = 1f, bond_type = "sing", length = 1.429f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "C5", bond_order = 1f, bond_type = "sing", length = 1.41f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "N4", bond_order = 1f, bond_type = "sing", length = 1.376f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "C3'", bond_order = 1f, bond_type = "sing", length = 1.549f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "H4'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "O4'", bond_order = 1f, bond_type = "sing", length = 1.445f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C6", bond_order = 2f, bond_type = "doub", length = 1.353f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "H5", bond_order = 1f, bond_type = "sing", length = 1.081f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "C4'", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5''", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "H6", bond_order = 1f, bond_type = "sing", length = 1.08f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C2", bond_order = 1f, bond_type = "sing", length = 1.344f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C6", bond_order = 1f, bond_type = "sing", length = 1.362f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "C4", bond_order = 2f, bond_type = "doub", length = 1.326f),
            KotmolBondRecord(atom_1 = "N4", atom_2 = "H41", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "N4", atom_2 = "H42", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "O3'", atom_2 = "HO3'", bond_order = 1f, bond_type = "sing", length = 0.968f),
            KotmolBondRecord(atom_1 = "O4'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.437f),
            KotmolBondRecord(atom_1 = "O5'", atom_2 = "C5'", bond_order = 1f, bond_type = "sing", length = 1.428f),
            KotmolBondRecord(atom_1 = "OP2", atom_2 = "HOP2", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "HOP3", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "P", bond_order = 1f, bond_type = "sing", length = 1.61f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "O5'", bond_order = 1f, bond_type = "sing", length = 1.611f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP1", bond_order = 2f, bond_type = "doub", length = 1.48f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP2", bond_order = 1f, bond_type = "sing", length = 1.609f
            )
    )
    val dg = listOf(
            /*
             *   !!!
             * These two records were added to capture the bond between the
             * C2' then O2' and HO2'
             * this was borrowed from another bond between an OXT and HXT
             * SEE: 1A1T.pdb
             */
            KotmolBondRecord(atom_1 = "O2'", atom_2 = "HO2'", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "O2'", bond_order = 1f, bond_type = "sing", length = 1.43f),
            /*
             *  End of added record
             */
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "H1'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "N9", bond_order = 1f, bond_type = "sing", length = 1.464f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N2", bond_order = 1f, bond_type = "sing", length = 1.372f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N3", bond_order = 2f, bond_type = "doub", length = 1.314f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.54f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2''", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "C2'", bond_order = 1f, bond_type = "sing", length = 1.547f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "H3'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "O3'", bond_order = 1f, bond_type = "sing", length = 1.43f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "C3'", bond_order = 1f, bond_type = "sing", length = 1.55f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "H4'", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "O4'", bond_order = 1f, bond_type = "sing", length = 1.446f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C4", bond_order = 1.5f, bond_type = "doub", length = 1.4f, aromatic = true),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C6", bond_order = 1f, bond_type = "sing", length = 1.415f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "C4'", bond_order = 1f, bond_type = "sing", length = 1.53f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5'", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5''", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "N1", bond_order = 1f, bond_type = "sing", length = 1.351f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "O6", bond_order = 2f, bond_type = "doub", length = 1.219f),
            KotmolBondRecord(atom_1 = "C8", atom_2 = "H8", bond_order = 1f, bond_type = "sing", length = 1.081f),
            KotmolBondRecord(atom_1 = "C8", atom_2 = "N7", bond_order = 1.5f, bond_type = "doub", length = 1.302f, aromatic = true),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C2", bond_order = 1f, bond_type = "sing", length = 1.362f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "H1", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "N2", atom_2 = "H21", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "N2", atom_2 = "H22", bond_order = 1f, bond_type = "sing", length = 0.97f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "C4", bond_order = 1f, bond_type = "sing", length = 1.338f),
            KotmolBondRecord(atom_1 = "N7", atom_2 = "C5", bond_order = 1.5f, bond_type = "sing", length = 1.355f, aromatic = true),
            KotmolBondRecord(atom_1 = "N9", atom_2 = "C4", bond_order = 1.5f, bond_type = "sing", length = 1.368f, aromatic = true),
            KotmolBondRecord(atom_1 = "N9", atom_2 = "C8", bond_order = 1.5f, bond_type = "sing", length = 1.364f, aromatic = true),
            KotmolBondRecord(atom_1 = "O3'", atom_2 = "HO3'", bond_order = 1f, bond_type = "sing", length = 0.966f),
            KotmolBondRecord(atom_1 = "O4'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.437f),
            KotmolBondRecord(atom_1 = "O5'", atom_2 = "C5'", bond_order = 1f, bond_type = "sing", length = 1.428f),
            KotmolBondRecord(atom_1 = "OP2", atom_2 = "HOP2", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "HOP3", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "P", bond_order = 1f, bond_type = "sing", length = 1.611f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "O5'", bond_order = 1f, bond_type = "sing", length = 1.61f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP1", bond_order = 2f, bond_type = "doub", length = 1.479f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP2", bond_order = 1f, bond_type = "sing", length = 1.608f
            )
    )
    val dt = listOf(
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "H1'", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "N1", bond_order = 1f, bond_type = "sing", length = 1.434f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N3", bond_order = 1f, bond_type = "sing", length = 1.399f),
            /* BUG should be O2
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2'", bond_order = 2f, bond_type = "doub", length = 1.232f),
            */
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2", bond_order = 2f, bond_type = "doub", length = 1.232f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.52f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2'", bond_order = 1f, bond_type = "sing", length = 1.097f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2''", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "C2'", bond_order = 1f, bond_type = "sing", length = 1.511f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "H3'", bond_order = 1f, bond_type = "sing", length = 1.094f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "O3'", bond_order = 1f, bond_type = "sing", length = 1.426f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "C5", bond_order = 1f, bond_type = "sing", length = 1.487f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "O4", bond_order = 2f, bond_type = "doub", length = 1.228f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "C3'", bond_order = 1f, bond_type = "sing", length = 1.522f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "H4'", bond_order = 1f, bond_type = "sing", length = 1.094f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "O4'", bond_order = 1f, bond_type = "sing", length = 1.441f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C6", bond_order = 2f, bond_type = "doub", length = 1.339f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C7", bond_order = 1f, bond_type = "sing", length = 1.495f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "C4'", bond_order = 1f, bond_type = "sing", length = 1.523f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5'", bond_order = 1f, bond_type = "sing", length = 1.092f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5''", bond_order = 1f, bond_type = "sing", length = 1.093f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "H6", bond_order = 1f, bond_type = "sing", length = 1.086f),
            KotmolBondRecord(atom_1 = "C7", atom_2 = "H71", bond_order = 1f, bond_type = "sing", length = 1.089f),
            KotmolBondRecord(atom_1 = "C7", atom_2 = "H72", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "C7", atom_2 = "H73", bond_order = 1f, bond_type = "sing", length = 1.09f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C2", bond_order = 1f, bond_type = "sing", length = 1.411f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C6", bond_order = 1f, bond_type = "sing", length = 1.389f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "C4", bond_order = 1f, bond_type = "sing", length = 1.387f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.016f),
            KotmolBondRecord(atom_1 = "O3'", atom_2 = "HO3'", bond_order = 1f, bond_type = "sing", length = 0.973f),
            KotmolBondRecord(atom_1 = "O4'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.434f),
            KotmolBondRecord(atom_1 = "O5'", atom_2 = "C5'", bond_order = 1f, bond_type = "sing", length = 1.418f),
            KotmolBondRecord(atom_1 = "OP2", atom_2 = "HOP2", bond_order = 1f, bond_type = "sing", length = 0.981f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "HOP3", bond_order = 1f, bond_type = "sing", length = 0.981f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "P", bond_order = 1f, bond_type = "sing", length = 1.618f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "O5'", bond_order = 1f, bond_type = "sing", length = 1.619f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP1", bond_order = 2f, bond_type = "doub", length = 1.501f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP2", bond_order = 1f, bond_type = "sing", length = 1.616f
            )
    )
    val du = listOf(
            /*
             *   !!!
             * These two records were added to capture the bond between the
             * C2' then O2' and HO2'
             * this was borrowed from another bond between an OXT and HXT
             * SEE: 1A1T.pdb
             */
            KotmolBondRecord(atom_1 = "O2'", atom_2 = "HO2'", bond_order = 1f, bond_type = "sing", length = 0.967f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "O2'", bond_order = 1f, bond_type = "sing", length = 1.43f),
            /*
             *  End of added record
             */


            KotmolBondRecord(atom_1 = "C1'", atom_2 = "H1'", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "C1'", atom_2 = "N1", bond_order = 1f, bond_type = "sing", length = 1.434f),
            KotmolBondRecord(atom_1 = "C2", atom_2 = "N3", bond_order = 1f, bond_type = "sing", length = 1.402f),
            /* BUG should be O2
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2'", bond_order = 2f, bond_type = "doub", length = 1.233f),
            */
            KotmolBondRecord(atom_1 = "C2", atom_2 = "O2", bond_order = 2f, bond_type = "doub", length = 1.233f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.52f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2'", bond_order = 1f, bond_type = "sing", length = 1.097f),
            KotmolBondRecord(atom_1 = "C2'", atom_2 = "H2''", bond_order = 1f, bond_type = "sing", length = 1.095f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "C2'", bond_order = 1f, bond_type = "sing", length = 1.511f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "H3'", bond_order = 1f, bond_type = "sing", length = 1.094f),
            KotmolBondRecord(atom_1 = "C3'", atom_2 = "O3'", bond_order = 1f, bond_type = "sing", length = 1.426f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "C5", bond_order = 1f, bond_type = "sing", length = 1.477f),
            KotmolBondRecord(atom_1 = "C4", atom_2 = "O4", bond_order = 2f, bond_type = "doub", length = 1.227f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "C3'", bond_order = 1f, bond_type = "sing", length = 1.522f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "H4'", bond_order = 1f, bond_type = "sing", length = 1.094f),
            KotmolBondRecord(atom_1 = "C4'", atom_2 = "O4'", bond_order = 1f, bond_type = "sing", length = 1.441f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "C6", bond_order = 2f, bond_type = "doub", length = 1.336f),
            KotmolBondRecord(atom_1 = "C5", atom_2 = "H5", bond_order = 1f, bond_type = "sing", length = 1.083f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "C4'", bond_order = 1f, bond_type = "sing", length = 1.523f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5'", bond_order = 1f, bond_type = "sing", length = 1.092f),
            KotmolBondRecord(atom_1 = "C5'", atom_2 = "H5''", bond_order = 1f, bond_type = "sing", length = 1.093f),
            KotmolBondRecord(atom_1 = "C6", atom_2 = "H6", bond_order = 1f, bond_type = "sing", length = 1.085f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C2", bond_order = 1f, bond_type = "sing", length = 1.412f),
            KotmolBondRecord(atom_1 = "N1", atom_2 = "C6", bond_order = 1f, bond_type = "sing", length = 1.389f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "C4", bond_order = 1f, bond_type = "sing", length = 1.386f),
            KotmolBondRecord(atom_1 = "N3", atom_2 = "H3", bond_order = 1f, bond_type = "sing", length = 1.016f),
            KotmolBondRecord(atom_1 = "O3'", atom_2 = "HO3'", bond_order = 1f, bond_type = "sing", length = 0.973f),
            KotmolBondRecord(atom_1 = "O4'", atom_2 = "C1'", bond_order = 1f, bond_type = "sing", length = 1.434f),
            KotmolBondRecord(atom_1 = "O5'", atom_2 = "C5'", bond_order = 1f, bond_type = "sing", length = 1.418f),
            KotmolBondRecord(atom_1 = "OP2", atom_2 = "HOP2", bond_order = 1f, bond_type = "sing", length = 0.981f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "HOP3", bond_order = 1f, bond_type = "sing", length = 0.981f),
            KotmolBondRecord(atom_1 = "OP3", atom_2 = "P", bond_order = 1f, bond_type = "sing", length = 1.618f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "O5'", bond_order = 1f, bond_type = "sing", length = 1.619f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP1", bond_order = 2f, bond_type = "doub", length = 1.501f),
            KotmolBondRecord(atom_1 = "P", atom_2 = "OP2", bond_order = 1f, bond_type = "sing", length = 1.616f
            )
    )


    val kotmolBondLookup = hashMapOf(
            "ala" to ala, "arg" to arg, "asn" to asn, "asp" to asp, "asx" to asx, "cys" to cys, "glu" to glu, "gln" to gln, "glx" to glx, "gly" to gly, "his" to his, "ile" to ile, "leu" to leu, "lys" to lys, "met" to met, "phe" to phe, "pro" to pro, "ser" to ser, "thr" to thr, "trp" to trp, "tyr" to tyr, "val" to valine, // nucleic acid bonds
            "da" to da, "dc" to dc, "dg" to dg, "dt" to dt, "du" to du, "a" to da, "c" to dc, "g" to dg, "t" to dt, "u" to du
    )

}
