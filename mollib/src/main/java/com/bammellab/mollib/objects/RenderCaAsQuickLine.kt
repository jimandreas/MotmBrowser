/*
 *  Copyright 2020 Bammellab / James Andreas
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
        "ConstantConditionIf",
        "LocalVariableName",
        "SameParameterValue")

package com.bammellab.mollib.objects

import com.bammellab.mollib.common.math.MotmVector3
import com.kotmol.pdbParser.ChainRenderingDescriptor
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.PdbAtom

/*
 * render the polypeptide backbone "ribbon" as a very fast GL_LINE
 */
class RenderCaAsQuickLine(private val mol: Molecule) {

    private lateinit var vertexData: FloatArray
    private var arrayOffset = 0
    val n = floatArrayOf(50.0f, 50.0f, 50.0f) // wired in random value for now

    fun renderQuickLine() {
        var chainDescriptorList: List<*>

        var chainEntry: ChainRenderingDescriptor
        var mainChainAtom: PdbAtom?
        var guideAtom: PdbAtom?
        var vnew: MotmVector3
        var j: Int
        val greenColor = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)


        for (i in 0 until mol.listofChainDescriptorLists.size) {
            chainDescriptorList = mol.listofChainDescriptorLists[i]
            if (chainDescriptorList.size < 2) {
                continue
            }

            j = 0

            vertexData = BufferManager.getFloatArray((chainDescriptorList.size-1) * 2 * STRIDE_IN_FLOATS)
            arrayOffset = BufferManager.floatArrayIndex

            while (j < chainDescriptorList.size) {
                chainEntry = chainDescriptorList[j]


                mainChainAtom = chainEntry.backboneAtom
                if (mainChainAtom != null) {
                    vnew = MotmVector3(mainChainAtom.atomPosition)

                    putPoint(vnew, greenColor)
                    // Lines in OpenGL are pairs of points.  Repeat the point
                    // if not at an end to set the first coordinate of the next line
                    if (j != 0 && j != chainDescriptorList.size - 1) {
                        putPoint(vnew, greenColor)
                    }
                }

                j++
            }
            BufferManager.floatArrayIndex = arrayOffset
            BufferManager.transferToGl()

        }
    }

    private fun putPoint(p1: MotmVector3, color: FloatArray ) {


        vertexData[arrayOffset++] = p1.x
        vertexData[arrayOffset++] = p1.y
        vertexData[arrayOffset++] = p1.z
        vertexData[arrayOffset++] = n[0]
        vertexData[arrayOffset++] = n[1]
        vertexData[arrayOffset++] = n[2]
        vertexData[arrayOffset++] = color[0]
        vertexData[arrayOffset++] = color[1]
        vertexData[arrayOffset++] = color[2]
        vertexData[arrayOffset++] = color[3]
    }



    companion object {

        private const val debug = false

        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT


        private const val INITIAL_SLICES = 20
        private const val RIBBON_INITIAL_SLICES = 30

    }
}