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

@file:Suppress("unused", "always_false")
package com.bammellab.mollib.objects

import com.bammellab.mollib.common.math.MathUtil
import com.bammellab.mollib.protein.Molecule
import com.bammellab.mollib.protein.PdbAtom

class AtomSphere(private val mMol: Molecule) {

    private val mNumIndices: Int = 0

    private val vbo = IntArray(1)
    private val ibo = IntArray(1)

    private val mBufMgr: BufferManager = mMol.mBufMgr

    fun genSphere(
            numSlices: Int,
            radiusIn: Float,
            atom: PdbAtom,
            colorIn: FloatArray /*RGBA*/
    ) {
        var radius = radiusIn
        var color = colorIn
        var i = 0
        var j: Int
        var vx1: Float
        var vy1: Float
        var vz1: Float
        var vx2: Float
        var vy2: Float
        var vz2: Float
        var vx3: Float
        var vy3: Float
        var vz3: Float
        var vx4: Float
        var vy4: Float
        var vz4: Float

        val whiteColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = mMol.dcOffset / 3

        val location = atom.atomPosition

        /*
         *  debugging picking
         */
        @Suppress("ConstantConditionIf")
        if (atom.pickedAtom) {
            color = whiteColor
            radius = 0.5f
            // Timber.i("OVERRIDE color atom " + atom.atom_number );
        }
        val angleStep = 2.0f * Math.PI.toFloat() / numSlices

        val vertexData = mBufMgr.getFloatArray(
                numSlices * numSlices // number of vertices

                        * 3 * 2 // two triangles worth generated per loop

                        * STRIDE_IN_FLOATS) // num floats per vertex
        var offset = mBufMgr.floatArrayIndex

        /*
         * note the use of less-than-equals - the first point is repeated to complete the circle
         */
        while (i < numSlices) {
            j = 0
            while (j < numSlices) {
                vx1 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * i.toFloat()).toDouble())
                        * MathUtil.sin((angleStep * j.toFloat()).toDouble())).toFloat()
                vy1 = (radius * MathUtil.cos((angleStep / 2.0f * i.toFloat()).toDouble())).toFloat()
                vz1 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * i.toFloat()).toDouble())
                        * MathUtil.cos((angleStep * j.toFloat()).toDouble())).toFloat()

                // down one latitude
                vx2 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * (i + 1).toFloat()).toDouble())
                        * MathUtil.sin((angleStep * j.toFloat()).toDouble())).toFloat()
                vy2 = (radius * MathUtil.cos((angleStep / 2.0f * (i + 1).toFloat()).toDouble())).toFloat()
                vz2 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * (i + 1).toFloat()).toDouble())
                        * MathUtil.cos((angleStep * j.toFloat()).toDouble())).toFloat()

                // down one longitude and over one latitude
                vx3 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * (i + 1).toFloat()).toDouble())
                        * MathUtil.sin((angleStep * (j + 1).toFloat()).toDouble())).toFloat()
                vy3 = (radius * MathUtil.cos((angleStep / 2.0f * (i + 1).toFloat()).toDouble())).toFloat()
                vz3 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * (i + 1).toFloat()).toDouble())
                        * MathUtil.cos((angleStep * (j + 1).toFloat()).toDouble())).toFloat()

                // over one longitude at same latitude
                vx4 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * i.toFloat()).toDouble())
                        * MathUtil.sin((angleStep * (j + 1).toFloat()).toDouble())).toFloat()
                vy4 = (radius * MathUtil.cos((angleStep / 2.0f * i.toFloat()).toDouble())).toFloat()
                vz4 = (radius.toDouble()
                        * MathUtil.sin((angleStep / 2.0f * i.toFloat()).toDouble())
                        * MathUtil.cos((angleStep * (j + 1).toFloat()).toDouble())).toFloat()


                // first top point
                vertexData[offset++] = vx1 + location.x.toFloat()
                vertexData[offset++] = vy1 + location.y.toFloat()
                vertexData[offset++] = vz1 + location.z.toFloat()

                vertexData[offset++] = vx1 / radius * normal_brightness_factor
                vertexData[offset++] = vy1 / radius * normal_brightness_factor
                vertexData[offset++] = vz1 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]

                // second (bottom)
                vertexData[offset++] = vx2 + location.x.toFloat()
                vertexData[offset++] = vy2 + location.y.toFloat()
                vertexData[offset++] = vz2 + location.z.toFloat()

                vertexData[offset++] = vx2 / radius * normal_brightness_factor
                vertexData[offset++] = vy2 / radius * normal_brightness_factor
                vertexData[offset++] = vz2 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]

                // third (bottom and over)
                vertexData[offset++] = vx3 + location.x.toFloat()
                vertexData[offset++] = vy3 + location.y.toFloat()
                vertexData[offset++] = vz3 + location.z.toFloat()

                vertexData[offset++] = vx3 / radius * normal_brightness_factor
                vertexData[offset++] = vy3 / radius * normal_brightness_factor
                vertexData[offset++] = vz3 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]

                // second triangle

                // first top point
                vertexData[offset++] = vx1 + location.x.toFloat()
                vertexData[offset++] = vy1 + location.y.toFloat()
                vertexData[offset++] = vz1 + location.z.toFloat()

                vertexData[offset++] = vx1 / radius * normal_brightness_factor
                vertexData[offset++] = vy1 / radius * normal_brightness_factor
                vertexData[offset++] = vz1 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]

                // second (bottom and over this time)
                vertexData[offset++] = vx3 + location.x.toFloat()
                vertexData[offset++] = vy3 + location.y.toFloat()
                vertexData[offset++] = vz3 + location.z.toFloat()

                vertexData[offset++] = vx3 / radius * normal_brightness_factor
                vertexData[offset++] = vy3 / radius * normal_brightness_factor
                vertexData[offset++] = vz3 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]

                // third (over)
                vertexData[offset++] = vx4 + location.x.toFloat()
                vertexData[offset++] = vy4 + location.y.toFloat()
                vertexData[offset++] = vz4 + location.z.toFloat()

                vertexData[offset++] = vx4 / radius * normal_brightness_factor
                vertexData[offset++] = vy4 / radius * normal_brightness_factor
                vertexData[offset++] = vz4 / radius * normal_brightness_factor

                vertexData[offset++] = color[0]
                vertexData[offset++] = color[1]
                vertexData[offset++] = color[2]
                vertexData[offset++] = color[3]
                j++

            }
            i++
        }

        mBufMgr.floatArrayIndex = offset

        /*
         * debugging print out of vertices
         */
//        i = 0
//        while (i < vertexData.size) {
//            vx = vertexData[i + 0]
//            vy = vertexData[i + 1]
//            vz = vertexData[i + 2]
//            val svx = String.format("%6.2f", vx)
//            val svy = String.format("%6.2f", vy)
//            val svz = String.format("%6.2f", vz)
//
//            Log.w("cyl ", (i / STRIDE_IN_FLOATS).toString() + " x y z "
//                    + svx + " " + svy + " " + svz + " and "
//                    + vertexData[i + 6] + " " + vertexData[i + 7] + " " + vertexData[i + 8])
//            i += STRIDE_IN_FLOATS
//        }


    }

    companion object {

        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

        private var normal_brightness_factor = 7f
    }

}
