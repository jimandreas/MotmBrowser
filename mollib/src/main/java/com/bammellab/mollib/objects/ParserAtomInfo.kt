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

package com.bammellab.mollib.objects

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.AssetManager
import timber.log.Timber

import com.bammellab.mollib.protein.AtomInfo

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.HashMap

/*
 * parse the atom_info_table and assemble the AtomInfo array
 *     each AtomInfo object is hashed in the mAtomNameToAtomInfoHash HashMap.
 *
 * NOTE:  The PDB info calls for only a single letter element_name,
 *  so that excludes all the multi-letter element names.
 *
 *  TODO:   see if there is a secret decoder ring for a special PDB character to element name mapping (??)
 */


@SuppressLint("DefaultLocale", "unused")
class ParserAtomInfo(activity: Activity) {

    val atomNameToAtomInfoHash = HashMap<String, AtomInfo>()

    private val mAtomInfo = ArrayList<AtomInfo>()

    private val assetManager: AssetManager = activity.assets

    fun parseAtomInfo() {
        parseAtomInfoFile(ATOM_INFO_FILE)
        parseVdwInfoFile(VDW_INFO_FILE)
    }


    private fun parseVdwInfoFile(atomInfoFileName: String) {
        val inputStream: InputStream?
        val reader: BufferedReader
        var atom: AtomInfo?
        var line: String? = null
        var lineNumber = 0
        try {
            inputStream = assetManager.open(atomInfoFileName, AssetManager.ACCESS_BUFFER)

            reader = BufferedReader(InputStreamReader(inputStream))

            line = reader.readLine()
            while (line != null) {
                if (lineNumber++ == 0) {
                    line = reader.readLine()
                    continue  // skip header line
                }
                val atomSymbol: String = line.substring(0, 3).trim { it <= ' ' }
                val vdwRadius: Int = Integer.valueOf(line.substring(5, 8))

                atom = atomNameToAtomInfoHash[atomSymbol]
                if (atom != null) {
                    atom.vdwRadius = vdwRadius
                }
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()
        } catch (e: IOException) {
            Timber.e("IO error in file $atomInfoFileName")
            if (line != null) {
                Timber.e("IO exception at line number $lineNumber line: $line")
            }
        }

    }

    private fun parseAtomInfoFile(atomInfoFileName: String) {
        val inputStream: InputStream?
        val reader: BufferedReader
        var line: String? = null
        var hexColor: String

        var lineNumber = 0
        try {
            inputStream = assetManager.open(atomInfoFileName, AssetManager.ACCESS_BUFFER)

            reader = BufferedReader(InputStreamReader(inputStream))
            line = reader.readLine()
            while (line != null) {
                lineNumber++
                // Timber.w("line is: " + line);

                val atomInfo = AtomInfo()

                atomInfo.atomAtomicNumber = parseInteger(line)
                atomInfo.atomChemicalSymbol = reader.readLine()
                atomInfo.atomElementName = reader.readLine()
                hexColor = reader.readLine()
                if (hexColor[0] != '#') {
                    Timber.e("format error in asset file: "
                            + atomInfoFileName
                            + " incorrect format for color at line: "
                            + lineNumber +
                            " expected #, got: " + hexColor)
                    line = reader.readLine()
                    continue
                }
                val color = FloatArray(4)

                var temp = Integer.valueOf(hexColor.substring(1, 3), 16)
                color[0] = temp.toFloat() / 255f
                temp = Integer.valueOf(hexColor.substring(3, 5), 16)
                color[1] = temp.toFloat() / 255f
                temp = Integer.valueOf(hexColor.substring(5, 7), 16)
                color[2] = temp.toFloat() / 255f

//                color[0] = Integer.valueOf(hex_color.substring(1, 3), 16) as Float / 255f
//                color[1] = Integer.valueOf(hex_color.substring(3, 5), 16) as Float / 255f
//                color[2] = Integer.valueOf(hex_color.substring(5, 7), 16) as Float / 255f
                color[3] = 1.0f

                atomInfo.color = color

                /*
                  atomRed = 255,
                    atomGreen = 255,
                    atomBlue = 255
                 */

println("zzzAtomInfo(")
println("zzzatomNumber = ${atomInfo.atomAtomicNumber},")
println("zzzatomChemicalSymbol = \"${atomInfo.atomChemicalSymbol}\",")
println("zzzatomElementName = \"${atomInfo.atomElementName}\",")
println("zzzatomRed = ${Integer.valueOf(hexColor.substring(1, 3), 16)},")
println("zzzatomGreen = ${Integer.valueOf(hexColor.substring(3, 5), 16)},")
println("zzzatomBlue = ${Integer.valueOf(hexColor.substring(5, 7), 16)}")

println("zzz),")
                mAtomInfo.add(atomInfo)
                atomNameToAtomInfoHash[atomInfo.atomChemicalSymbol] = atomInfo
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()
        } catch (e: IOException) {
            Timber.e("IO error in file $atomInfoFileName")
            if (line != null) {
                Timber.e("IO exception at line number $lineNumber line: $line")
            }
        }
println("zzzdone")

    }

    private fun parseInteger(s: String): Int {
        return try {
            Integer.parseInt(s)
        } catch (e: RuntimeException) {
            Timber.e("Bad Integer : $s")
            0
        }

    }

    companion object {
        private const val ATOM_INFO_FILE = "z_atom_info_table"
        private const val VDW_INFO_FILE = "z_vdw_radii_table"
    }
}
