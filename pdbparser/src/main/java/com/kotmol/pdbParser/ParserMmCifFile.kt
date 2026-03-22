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

@file:Suppress(
    "unused",
    "unused_variable",
    "unused_parameter",
    "MemberVisibilityCanBePrivate",
    "JoinDeclarationAndAssignment"
)

package com.kotmol.pdbParser

import java.io.InputStream

/**
 * Parser for mmCIF/PDBx format files.
 *
 * Mirrors the [ParserPdbFile] Builder API so callers can switch formats with minimal changes.
 * Both parsers populate the same [Molecule] container, so all downstream rendering code
 * (mollib / OpenGL) works unchanged.
 *
 * Usage:
 * ```kotlin
 * ParserMmCifFile
 *     .Builder(mol)
 *     .setMoleculeName("4HHB")
 *     .setMessageStrings(retainedMessages)
 *     .loadMmCifFromStream(inputStream)
 *     .doBondProcessing(true)
 *     .parse()
 * ```
 *
 * Note: use [loadMmCifFromStream] (not `loadPdbFromStream`) to distinguish the two parsers
 * at the call site.
 *
 * Post-processing methods (mapBonds, buildPdbChainLists, addHelixSecondaryInformation,
 * addSheetSecondaryInformation, connectResidues, centerMolecule) are copied from
 * [ParserPdbFile] — Option A of the plan — so this class has zero dependency on the
 * PDB parser and neither file needs to be modified.
 */
class ParserMmCifFile internal constructor(builder: Builder) {

    constructor(mol: Molecule) : this(Builder(mol))

    class Builder(private val mol: Molecule) {

        private val bondinfo = BondInfo()
        private var averageX = 0.0f
        private var averageY = 0.0f
        private var averageZ = 0.0f
        private lateinit var messageStrings: MutableList<String>
        private var centerTheMoleculeCoordinatesFlag = true
        private var doBondProcessingFlag = true
        private lateinit var inputStream: InputStream
        private val encodedBondMap: MutableMap<Int, Boolean> = mutableMapOf(0 to false)

        fun setMessageStrings(messagesIn: MutableList<String>) = apply {
            messageStrings = messagesIn
        }

        fun setMoleculeName(molNameString: String) = apply {
            mol.molName = molNameString
        }

        fun doBondProcessing(bool: Boolean) = apply {
            doBondProcessingFlag = bool
        }

        fun centerTheMolecule(centerTheMoleculeFlag: Boolean) = apply {
            centerTheMoleculeCoordinatesFlag = centerTheMoleculeFlag
        }

        fun loadMmCifFromStream(theStreamToUse: InputStream) = apply {
            inputStream = theStreamToUse
        }

        fun parse() = apply {
            if (!this::inputStream.isInitialized) {
                throw IllegalStateException("loadMmCifFromStream() must be called before parse()")
            }
            if (!this::messageStrings.isInitialized) {
                messageStrings = mutableListOf("")
            }

            val parser = MmCifParser(mol, messageStrings)
            parser.parse(inputStream)

            // Compute average position from atom site handler
            if (mol.atomNumberList.isNotEmpty()) {
                val avgPos = parser.atomSiteHandler.getAveragePosition()
                averageX = avgPos.x
                averageY = avgPos.y
                averageZ = avgPos.z
            }
            mol.averagePosition.x = averageX
            mol.averagePosition.y = averageY
            mol.averagePosition.z = averageZ

            if (doBondProcessingFlag) {
                mapBonds()
                buildPdbChainLists()
                addHelixSecondaryInformation()
                addSheetSecondaryInformation()
                connectResidues()
            }

            if (centerTheMoleculeCoordinatesFlag) centerMolecule()
        }

        // =====================================================================
        // Post-processing methods — copied from ParserPdbFile.Builder (Option A)
        // These operate exclusively on the Molecule object; no PDB-specific logic.
        // =====================================================================

        private fun mapBonds() {
            val resnameToBonds = bondinfo.kotmolBondLookup
            var residueName: String
            var anAtom: PdbAtom?
            var residueSequenceNumber: Int
            var residueInsertionCode: Char
            var warnOnUnknownResidue = false

            var i = 0
            while (i < mol.atomNumberList.size) {
                anAtom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
                requireNotNull(anAtom)
                if (anAtom.atomType == PdbAtom.AtomType.IS_HETATM
                    || anAtom.atomType == PdbAtom.AtomType.IS_TER_RECORD) {
                    i++
                    continue
                }
                residueSequenceNumber = anAtom.residueSeqNumber
                residueInsertionCode = anAtom.residueInsertionCode
                residueName = anAtom.residueName.lowercase()

                if (!resnameToBonds.containsKey(residueName)) {
                    if (residueName == "unk") {
                        if (!warnOnUnknownResidue) {
                            messageStrings.add("matchBonds: UNK (unknown) residues present in mmCIF file")
                            warnOnUnknownResidue = true
                        }
                        i++
                        continue
                    }
                    messageStrings.add(
                        String.format(
                            "matchBonds: no matching info for residue %s at atom %d",
                            residueName, anAtom.atomNumber
                        )
                    )
                    i++
                    continue
                }
                val bondMap = resnameToBonds[residueName]
                requireNotNull(bondMap)
                matchBonds(i, residueName, residueSequenceNumber, residueInsertionCode, bondMap)
                i++
                while (i < mol.atomNumberList.size) {
                    val atomSerialNumber = mol.atomNumberList[i]
                    anAtom = mol.atomNumberToAtomInfoHash[atomSerialNumber]
                    requireNotNull(anAtom)
                    if (anAtom.residueSeqNumber != residueSequenceNumber
                        || anAtom.residueInsertionCode != residueInsertionCode
                        || anAtom.residueName.lowercase() != residueName
                    ) {
                        break
                    }
                    i++
                    if (mol.terRecordTest[atomSerialNumber + 1] == true) {
                        break
                    }
                }
                if (i == mol.atomNumberToAtomInfoHash.size) {
                    break
                } else {
                    i--
                }
                i++
            }
        }

        private fun matchBonds(
            atomIndex: Int,
            residueName: String,
            residueSequenceNumber: Int,
            residueInsertionCode: Char,
            bondListOriginal: List<BondInfo.KotmolBondRecord>
        ) {
            var currentAtom: PdbAtom?
            var loopAtom: PdbAtom?
            val bondList = ArrayList(bondListOriginal.map { it.copy() })

            var i = atomIndex
            var j = 0
            while (true) {
                if (i >= mol.atomNumberList.size) break
                var atomSerialNumber = mol.atomNumberList[i]
                currentAtom = mol.atomNumberToAtomInfoHash[atomSerialNumber]
                if (currentAtom == null) { i++; continue }

                if (currentAtom.residueSeqNumber != residueSequenceNumber
                    || currentAtom.residueInsertionCode != residueInsertionCode
                    || currentAtom.residueName.lowercase() != residueName.lowercase()
                ) {
                    break
                }

                while (true) {
                    val bondAtomName = findNextBond(currentAtom.atomName, bondList)
                    if (bondAtomName == "") break

                    j = i + 1
                    while (true) {
                        if (j >= mol.atomNumberList.size) break
                        atomSerialNumber = mol.atomNumberList[j]
                        loopAtom = mol.atomNumberToAtomInfoHash[atomSerialNumber]
                        if (loopAtom == null) { j++; continue }

                        if (loopAtom.residueSeqNumber != residueSequenceNumber
                            || loopAtom.residueInsertionCode.lowercaseChar() != residueInsertionCode.lowercaseChar()
                        ) {
                            break
                        }
                        if (loopAtom.atomName == bondAtomName) {
                            addBond(currentAtom, loopAtom)
                            break
                        }
                        j++
                        if (mol.terRecordTest[atomSerialNumber + 1] == true) break
                    }
                }

                if (currentAtom.atomBondCount == 0) {
                    mol.unbondedAtomCount++
                    if (j != i + 1) {
                        messageStrings.add(
                            String.format(
                                "matchBonds: pdbName: %s no Bond Atom for atom %d in residue %s atomType %s",
                                mol.molName, currentAtom.atomNumber,
                                currentAtom.residueName, currentAtom.atomName
                            )
                        )
                    }
                }

                atomSerialNumber = mol.atomNumberList[i]
                if (mol.terRecordTest[atomSerialNumber + 1] == true) break
                i++
            }
        }

        private fun findNextBond(
            atomName: String,
            bondList: ArrayList<BondInfo.KotmolBondRecord>
        ): String {
            for (bond in bondList) {
                if (bond.bondRecordCreated) continue
                if (atomName == bond.atom_1) { bond.bondRecordCreated = true; return bond.atom_2 }
                if (atomName == bond.atom_2) { bond.bondRecordCreated = true; return bond.atom_1 }
            }
            return ""
        }

        private fun addBond(atom1: PdbAtom, atom2: PdbAtom): Bond? {
            if (atom1.atomBondCount > 0 && atom2.atomBondCount > 0) {
                val enc1 = atom1.atomNumber shl 16 or atom2.atomNumber
                val enc2 = atom2.atomNumber shl 16 or atom1.atomNumber
                if (encodedBondMap[enc1] != null) return null
                if (encodedBondMap[enc2] != null) return null
            }
            val bond = Bond(atom1.atomNumber, atom2.atomNumber)
            mol.bondList.add(bond)
            val enc = atom1.atomNumber shl 16 or atom2.atomNumber
            encodedBondMap[enc] = true
            atom1.atomBondCount++
            atom2.atomBondCount++
            return bond
        }

        private fun buildPdbChainLists() {
            if (mol.atomNumberList.isEmpty()) return

            var anAtom: PdbAtom?
            var chainList: MutableList<ChainRenderingDescriptor> = ArrayList()
            var chain = ChainRenderingDescriptor()

            anAtom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[0]]
            if (anAtom == null) {
                messageStrings.add("buildPdbChainLists: error - first atom is null!")
                return
            }
            var currentChainIdChar = anAtom.chainId
            var residueSequenceNumber = anAtom.residueSeqNumber

            for (i in 0 until mol.atomNumberList.size) {
                anAtom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
                if (anAtom == null) {
                    messageStrings.add(
                        String.format("buildPdbChainLists: error - got null for %d", mol.atomNumberList[i])
                    )
                    continue
                }
                if (anAtom.atomType == PdbAtom.AtomType.IS_TER_RECORD) continue

                if (residueSequenceNumber != anAtom.residueSeqNumber) {
                    residueSequenceNumber = anAtom.residueSeqNumber
                    if (chain.backboneAtom != null) {
                        chainList.add(chain)
                        if (chain.guideAtom == null) {
                            if (!mol.guideAtomMissing) {
                                messageStrings.add(
                                    String.format(
                                        "%sbuildPdbChainLists: no guide atom in residue at atom %d",
                                        messageMolName(), anAtom.atomNumber
                                    )
                                )
                                mol.guideAtomMissing = true
                            }
                            chain.guideAtom = chain.backboneAtom
                        }
                        chain = ChainRenderingDescriptor()
                    }
                }

                if (currentChainIdChar != anAtom.chainId) {
                    currentChainIdChar = anAtom.chainId
                    if (chainList.size > 2) {
                        mol.listofChainDescriptorLists.add(chainList)
                        mol.ribbonNodeCount += chainList.size
                        chainList = ArrayList()
                    } else {
                        chainList.clear()
                    }
                }

                if (anAtom.residueName == "HOH") continue
                if (anAtom.atomType == PdbAtom.AtomType.IS_HETATM) continue

                when (anAtom.atomName) {
                    "CA" -> chain.backboneAtom = anAtom
                    "O"  -> chain.guideAtom    = anAtom
                    "N"  -> chain.startAtom    = anAtom
                    "C"  -> chain.endAtom      = anAtom
                }
                when (anAtom.atomName) {
                    "C5'" -> {
                        chain.backboneAtom = anAtom
                        chain.secondaryStructureType = ChainRenderingDescriptor.SecondaryStructureType.NUCLEIC
                    }
                    "C1'" -> chain.guideAtom  = anAtom
                    "O5'" -> chain.startAtom  = anAtom
                    "O3'" -> chain.endAtom    = anAtom
                    "C3'" -> chain.nucleicEndAtom = anAtom
                }

                if (anAtom.residueName == "DC" || anAtom.residueName == "DT"
                    || anAtom.residueName == "C"  || anAtom.residueName == "T"
                    || anAtom.residueName == "U"
                ) {
                    chain.nucleicType = ChainRenderingDescriptor.NucleicType.PURINE
                    when (anAtom.atomName) {
                        "N1" -> { chain.nucleicCornerAtom = anAtom; anAtom.atomType = PdbAtom.AtomType.IS_NUCLEIC }
                        "C2" -> chain.nucleicGuideAtom  = anAtom
                        "C6" -> chain.nucleicPlanarAtom = anAtom
                    }
                } else if (anAtom.residueName == "DA" || anAtom.residueName == "DG"
                    || anAtom.residueName == "A"  || anAtom.residueName == "G"
                    || anAtom.residueName == "8OG"
                ) {
                    chain.nucleicType = ChainRenderingDescriptor.NucleicType.PYRIMIDINE
                    when (anAtom.atomName) {
                        "N9" -> { chain.nucleicCornerAtom = anAtom; anAtom.atomType = PdbAtom.AtomType.IS_NUCLEIC }
                        "C4" -> chain.nucleicGuideAtom  = anAtom
                        "N7" -> chain.nucleicPlanarAtom = anAtom
                    }
                }

                if (anAtom.atomName == "C1'" || anAtom.atomName == "C2'"
                    || anAtom.atomName == "C3'" || anAtom.atomName == "C4'"
                    || anAtom.atomName == "O4'"
                ) {
                    anAtom.atomType = PdbAtom.AtomType.IS_NUCLEIC
                }
            }

            if (chainList.size > 2) {
                if (chain.backboneAtom != null) chainList.add(chain)
                mol.listofChainDescriptorLists.add(chainList)
                mol.ribbonNodeCount += chainList.size
            } else {
                chainList.clear()
            }
        }

        private fun addHelixSecondaryInformation() {
            var i: Int
            var j = 0
            val alphaHelixList = mol.helixList
            if (alphaHelixList.isEmpty()) return

            var list: List<ChainRenderingDescriptor>? = null

            for (listCount in alphaHelixList.indices) {
                val pdbHelix = alphaHelixList[listCount]
                val initialResidueNumber  = pdbHelix.initialResidueNumber
                val initialChainIdChar    = pdbHelix.initialChainIdChar
                val terminalResidueNumber = pdbHelix.terminalResidueNumber
                val terminalChainIdChar   = pdbHelix.terminalChainIdChar

                val listOfLists = mol.listofChainDescriptorLists
                var found = false
                i = 0
                while (i < listOfLists.size) {
                    list = listOfLists[i]
                    j = 0
                    while (j < list.size) {
                        val item = list[j]
                        val backbone = item.backboneAtom
                        if (backbone != null
                            && backbone.chainId == initialChainIdChar
                            && backbone.residueSeqNumber == initialResidueNumber
                        ) {
                            item.secondaryStructureType =
                                ChainRenderingDescriptor.SecondaryStructureType.ALPHA_HELIX
                            found = true
                            break
                        }
                        j++
                    }
                    if (found) break
                    i++
                }
                if (!found) {
                    messageStrings.add(
                        String.format(
                            "HELIX residue not found - number %d chain char %c",
                            initialResidueNumber, initialChainIdChar
                        )
                    )
                    continue
                }
                var nextItem: ChainRenderingDescriptor? = null
                while (j < list!!.size - 1) {
                    nextItem = list[j + 1]
                    nextItem.secondaryStructureType =
                        ChainRenderingDescriptor.SecondaryStructureType.ALPHA_HELIX
                    val backbone = nextItem.backboneAtom
                    if (backbone != null
                        && backbone.chainId == terminalChainIdChar
                        && backbone.residueSeqNumber == terminalResidueNumber
                    ) {
                        nextItem.endOfSecondaryStructure = true
                        break
                    }
                    j++
                }
                if (nextItem != null && !nextItem.endOfSecondaryStructure) {
                    messageStrings.add(
                        String.format(
                            "terminating HELIX residue not found- number %d chain char %c",
                            terminalResidueNumber, terminalChainIdChar
                        )
                    )
                    nextItem.endOfSecondaryStructure = true
                }
            }
        }

        private fun addSheetSecondaryInformation() {
            var i: Int
            var j: Int
            val betaSheetList = mol.pdbSheetList
            if (betaSheetList.isEmpty()) return

            var list: List<ChainRenderingDescriptor>? = null

            for (listCount in betaSheetList.indices) {
                val pdbSheet = betaSheetList[listCount]
                val initialResidueNumber  = pdbSheet.initialResidueNumber
                val initialChainIdChar    = pdbSheet.initialChainIdChar
                val terminalResidueNumber = pdbSheet.terminalResidueNumber
                val terminalChainIdChar   = pdbSheet.terminalChainIdChar

                val listOfLists = mol.listofChainDescriptorLists
                var found = false
                i = 0
                while (i < listOfLists.size) {
                    list = listOfLists[i]
                    j = 0
                    while (j < list.size) {
                        val item = list[j]
                        val backbone = item.backboneAtom
                        if (backbone != null
                            && backbone.chainId == initialChainIdChar
                            && backbone.residueSeqNumber == initialResidueNumber
                        ) {
                            item.secondaryStructureType =
                                ChainRenderingDescriptor.SecondaryStructureType.BETA_SHEET
                            found = true
                            break
                        }
                        j++
                    }
                    if (found) break
                    i++
                }
                if (!found) {
                    messageStrings.add(
                        String.format(
                            "SHEET residue not found - number %d chain char: %c",
                            initialResidueNumber, initialChainIdChar
                        )
                    )
                    continue
                }
                var nextItem: ChainRenderingDescriptor? = null
                j = 0
                while (j < list!!.size - 1) {
                    nextItem = list[j + 1]
                    nextItem.secondaryStructureType =
                        ChainRenderingDescriptor.SecondaryStructureType.BETA_SHEET
                    val backbone = nextItem.backboneAtom
                    if (backbone != null
                        && backbone.chainId == terminalChainIdChar
                        && backbone.residueSeqNumber == terminalResidueNumber
                    ) {
                        nextItem.endOfSecondaryStructure = true
                        break
                    }
                    j++
                }
                if (nextItem != null && !nextItem.endOfSecondaryStructure) {
                    messageStrings.add(
                        String.format(
                            "SHEET terminating residue not found - number %d chain char: %c",
                            initialResidueNumber, initialChainIdChar
                        )
                    )
                    nextItem.endOfSecondaryStructure = true
                }
            }
        }

        private fun connectResidues() {
            var totalDistance = 0f
            var count = 0
            var anAtom: PdbAtom?
            var lastAtom: PdbAtom? = null
            var lastResidueSequenceNumber = 0

            for (i in 0 until mol.atomNumberList.size) {
                anAtom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
                if (anAtom == null) {
                    messageStrings.add(
                        String.format("connectResidues: error - got null for %s", mol.atomNumberList[i])
                    )
                    continue
                }
                if (anAtom.atomType == PdbAtom.AtomType.IS_HETATM) continue

                if (anAtom.atomName == "O3'" || anAtom.atomName == "C") {
                    lastResidueSequenceNumber = anAtom.residueSeqNumber
                    lastAtom = anAtom
                } else if (anAtom.atomName == "P" || anAtom.atomName == "N") {
                    if (anAtom.residueSeqNumber == lastResidueSequenceNumber + 1 && lastAtom != null) {
                        val dist = anAtom.atomPosition.distanceTo(lastAtom.atomPosition)
                        if (dist < 2.0) {
                            totalDistance += dist
                            count++
                            addBond(anAtom, lastAtom)
                        } else {
                            val avgDist = if (count > 0) totalDistance / count.toFloat() else 0f
                            messageStrings.add(
                                String.format(
                                    "connectResidues: excessive bond dist = %6.2f from atom %s to %s",
                                    avgDist, lastAtom.atomNumber, anAtom.atomNumber
                                )
                            )
                        }
                        lastAtom = null
                    }
                }
            }
        }

        private fun centerMolecule() {
            var maxVector = KotmolVector3()
            var maxVectorMagnitude = 0.0f

            for (i in 0 until mol.atomNumberList.size) {
                val anAtom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
                if (anAtom == null) {
                    messageStrings.add(
                        String.format("centerMolecule: error - got null for %d", mol.atomNumberList[i])
                    )
                    continue
                }
                if (anAtom.atomType == PdbAtom.AtomType.IS_TER_RECORD) continue

                anAtom.atomPosition.x -= averageX
                anAtom.atomPosition.y -= averageY
                anAtom.atomPosition.z -= averageZ

                val vector = kotlin.math.sqrt(
                    anAtom.atomPosition.x * anAtom.atomPosition.x +
                    anAtom.atomPosition.y * anAtom.atomPosition.y +
                    anAtom.atomPosition.z * anAtom.atomPosition.z
                )
                if (vector > maxVectorMagnitude) {
                    maxVector = KotmolVector3(
                        anAtom.atomPosition.x,
                        anAtom.atomPosition.y,
                        anAtom.atomPosition.z
                    )
                    maxVectorMagnitude = vector
                }
            }

            mol.maxPostCenteringVectorMagnitude = maxVectorMagnitude
            mol.maxPostCenteringCoordinate      = maxVector
        }

        private fun messageMolName(): String =
            if (mol.molName.isNotEmpty()) "${mol.molName}: " else ""
    }
}
