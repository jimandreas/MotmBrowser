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

@file:Suppress("unused")
package com.bammellab.mollib.objects

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.AssetManager
import android.os.SystemClock

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.HashMap

import timber.log.Timber

/*
 * parse the CHARMM files for amino acid bonding info
 */

/*
 * Notes on implementation:
 *
 * Use of Guava multimap:
 * https://github.com/google/guava/wiki/CollectionUtilitiesExplained#multimaps
 * http://stackoverflow.com/questions/8229473/hashmap-one-key-multiple-values
 */
@Suppress("CascadeIf")
@SuppressLint("DefaultLocale")
class ParserBondTemplate(activity: Activity) {

    private val debugOutput = false

    val residueToBondHash = HashMap<String, ArrayListMultimap<String, String>>()
    // for debugging
    private val residueList = ArrayList<String>()
    private val mAssetManager: AssetManager = activity.assets

    fun parseBondInfo() {

        // TODO: keep for debug, then toss
        val multiMap = ArrayListMultimap.create<String, String>()
        multiMap.put("foo", "1")
        multiMap.put("bar", "2")
        multiMap.put("bar", "3")
        multiMap.put("bar", "99")

        val startTime = SystemClock.uptimeMillis().toFloat()

        parseCHARMMfile(DNA_RESIDUE_BOND_INFOFILE)
        parseCHARMMfile(PROTEIN_RESIDUE_BOND_INFOFILE)
        parseCHARMMfile(RNA_BOND_INFOFILE)

        val elapsedTime = (SystemClock.uptimeMillis() - startTime) / 1000
        val prettyPrint = String.format("%6.2f", elapsedTime)

        @Suppress("ConstantConditionIf")
        if (debugOutput) Timber.i("finished parsing in $prettyPrint seconds.")

    }

    private fun parseCHARMMfile(charmmFileName: String) {
        val inputStream: InputStream?
        val reader: BufferedReader

        var residueBondMap: ArrayListMultimap<String, String> = ArrayListMultimap.create()
        var residueName: String? = null
        var lineNumber = 0
        var line: String? = null
        try {
            inputStream = mAssetManager.open(charmmFileName, AssetManager.ACCESS_BUFFER)
            if (inputStream == null) {
                Timber.e("cannot open$charmmFileName, returning")
                return
            }

            reader = BufferedReader(InputStreamReader(inputStream))

            line = reader.readLine()
            while (line != null) {
                lineNumber++
                // Timber.w("line is: " + line);
                if (line.length < 7) {
                    line = reader.readLine()
                    continue
                }
                /*
                 * since the "RESI" is in a wired-in file,
                 * we can be fast and loose with the parsing
                 */

                if (line.substring(0, 4).contains("RESI")) {
                    residueName = line.substring(5, 8).trim { it <= ' ' }
                    if (residueName == "ALA") {
                        // special case for parsing "ALAD"
                        if (line.substring(5, 9).trim { it <= ' ' } == "ALAD") {
                            residueName = "ALAD"
                        }
                    }
                    residueBondMap = ArrayListMultimap.create()
                    residueToBondHash[residueName] = residueBondMap
                    //                        mResidueBondMappingBundle.putBundle(residue_name, bond_map);
                    residueList.add(residueName)  // for dumping the bundle

                } else if (line.substring(0, 4).contains("BOND")) {
                    parseBondLine(line.substring(4), residueBondMap, residueName)
                } else if (line.substring(0, 6).contains("DOUBLE")) {
                    parseBondLine(line.substring(6), residueBondMap, residueName)
                }
                line = reader.readLine()
            }
            // end of loop - enter the last RESI to the Hash

            reader.close()
            inputStream.close()
        } catch (e: IOException) {
            Timber.e(e, "IO error in file $charmmFileName")
            if (line != null) {
                Timber.e("IO exception at line number $lineNumber line: $line")
            }
        }

        /*
         * debugging
         */
        @Suppress("ConstantConditionIf")
        if (debugOutput) {
            for (i in residueList.indices) {
                val rname = residueList[i]
                val map = residueToBondHash[rname]
                val dump = map.toString()
                Timber.i("res: $rname dumpString: $dump")
            }
        }


        /*
         * formatted "code" - see below for template
         */

        if (debugOutput) {
            for (i in residueList.indices) {
                val rname = residueList[i]
                Timber.i("res: \"$rname\" to hashMapOf(")
                val map = residueToBondHash[rname]

                var sepchar = ","
                map.apply {

                    for (item in this!!.asMap()) {

                        var theString:String = ""
                        for (str in item.value) {

                            theString += "\"$str\", "
                        }

                        theString = theString.substring(0, theString.length-2)
                        Timber.i("res:  \"${item.key}\" to listOf($theString),")

                    }
                }

                Timber.i("res: ),")
            }
        }
//        println("res: PRINTLN")
    }

    /*
        val map3 = hashMapOf(
        "VAL" to hashMapOf(
            "OXY" to listOf("H1", "H2", "H3"),
            "N1" to listOf("C1", "C2", "C3")
        ),
        "ARG" to hashMapOf(
            "OXY" to listOf("H1", "H2", "H3"),
            "N1" to listOf("C1", "C2", "C3")
        )
    )
     */

    private fun parseBondLine(line: String, bond_map: Multimap<String, String>?, rname: String?) {

        var firstAtom = line
        var secondAtom: String
        var restToProcess: String

        // Timber.w("parseBondLine: " + line);

        /*
         * parse up to 10 pairs of atom identifiers
         */
        for (i in 0..9) {
            firstAtom = firstAtom.trim { it <= ' ' }
            if (firstAtom.isEmpty()) {
                break
            }
            val secondSpaceIndex = firstAtom.indexOf(' ') + 1
            secondAtom = firstAtom.substring(secondSpaceIndex)
            secondAtom = secondAtom.trim { it <= ' ' }
            val thirdSpaceIndex = secondAtom.indexOf(' ') + 1
            restToProcess = secondAtom.substring(thirdSpaceIndex)

            firstAtom = firstAtom.substring(0, secondSpaceIndex - 1)
            if (thirdSpaceIndex != 0) {
                secondAtom = secondAtom.substring(0, thirdSpaceIndex - 1)
            }

            // CHARMM file has +N designator for the nitrogen in the next
            //  residue.   Skip this as these bonds will be dealt with later
            //  on a scan of the residues.
            if (firstAtom != "+N" && secondAtom != "+N") {
                // update atom names CHARMM -> PDB
                firstAtom = editAtomNames(firstAtom, rname)
                secondAtom = editAtomNames(secondAtom, rname)
                bond_map!!.put(firstAtom, secondAtom)
            }
            // Timber.w("   " + first_atom + ":" + second_atom);
            if (thirdSpaceIndex != 0) {
                firstAtom = restToProcess
            } else
                break
        }
    }

    /*
     * do the editing here of the CHARMM atom naming to PDB naming.
     *    UGLY but necessary
     */
    private fun editAtomNames(atom_nameIn: String, rname: String?): String {
        var atomName = atom_nameIn

        if (atomName == "HN") { // hydrogen bound to backbone nitrogen
            atomName = "H"
        } else if (rname == "ARG"
                || rname == "ASN"
                || rname == "ASP"

                || rname == "GLN"
                || rname == "GLU"
                || rname == "LYS"
                || rname == "PRO") {
            if (atomName == "HB1")
                atomName = "HB2"
            else if (atomName == "HB2")
                atomName = "HB3"
            else if (atomName == "HG1")
                atomName = "HG2"
            else if (atomName == "HG2")
                atomName = "HG3"
            else if (atomName == "HD1")
                atomName = "HD2"
            else if (atomName == "HD2")
                atomName = "HD3"
            else if (atomName == "HE1")
                atomName = "HE2"
            else if (atomName == "HE2") atomName = "HE3"

        } else if (rname == "GLY" // note funky : BOND C +N in GLY CHARMM
        ) {
            if (atomName == "HA1")
                atomName = "HA2"
            else if (atomName == "HA2") atomName = "HA3"

        } else if (rname == "HIS" //

                || rname == "HSE"
                || rname == "HSP"
                || rname == "LEU"
                || rname == "PHE"
                || rname == "TRP"
                || rname == "TYR") {
            if (atomName == "HB1")
                atomName = "HB2"
            else if (atomName == "HB2") atomName = "HB3"
        } else if (rname == "ILE" //
        ) {
            if (atomName == "HG11")
                atomName = "HG12"
            else if (atomName == "HG12")
                atomName = "HG13"
            else if (atomName == "CD")
                atomName = "CD1"
            else if (atomName == "HD1")
                atomName = "HD11"
            else if (atomName == "HD1")
                atomName = "HD11"
            else if (atomName == "HD2")
                atomName = "HD12"
            else if (atomName == "HD3") atomName = "HD13"
        } else if (rname == "MET" //
        ) {
            if (atomName == "HB1")
                atomName = "HB2"
            else if (atomName == "HB2")
                atomName = "HB3"
            else if (atomName == "HG1")
                atomName = "HG2"
            else if (atomName == "HG2") atomName = "HG3"
        } else if (rname == "SER" //
                || rname == "CYS") {
            if (atomName == "HB1")
                atomName = "HB2"
            else if (atomName == "HB2")
                atomName = "HB3"
            else if (atomName == "HG1") atomName = "HG"
        } else if (rname == "DT" //
        ) {
            if (atomName == "C5M")
                atomName = "C7"
            else if (atomName == "H51")
                atomName = "H71"
            else if (atomName == "H52")
                atomName = "H72"
            else if (atomName == "H53") atomName = "H73"
        }
        return atomName
    }

    private fun parseFloat(s: String): Float {
        return try {
            java.lang.Float.parseFloat(s)
        } catch (e: RuntimeException) {
            0f
        }
    }

    private fun parseInteger(s: String): Int {
        return try {
            Integer.parseInt(s)
        } catch (e: RuntimeException) {
            Timber.e(e, "Bad Integer : $s")
            0
        }
    }

    companion object {
        private const val DNA_RESIDUE_BOND_INFOFILE = "top_all36_na.rtf"
        private const val PROTEIN_RESIDUE_BOND_INFOFILE = "top_all36_prot.rtf"
        private const val RNA_BOND_INFOFILE = "rna.rtf"
    }
}
