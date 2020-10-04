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
package com.bammellab.mollib.objects

import android.opengl.GLES20
import com.bammellab.mollib.common.math.MathUtil
import com.bammellab.mollib.common.math.Vector3
import com.bammellab.mollib.protein.Molecule
import java.nio.ByteBuffer
import java.nio.ByteOrder

/*
 * Backbone segment - just a cylinder with normalized (orthogonal) ends (no end caps yet)
 */
class SegmentBackboneDirect(private val mMol: Molecule) {
    // private static final float ELLIPSE_X_FACTOR = 2f / 9f;

    private var numIndices = 0
    private val cylinderIndexCount: Int = 0
    private lateinit var floatData: FloatArray
    private var mOffset: Int = 0

    private val vbo = IntArray(1)
    internal val ibo = IntArray(1)

    private val bufMgr: BufferManager = mMol.bufMgr

    // TODO:  work on color for atom ends and half-way in the connection

    fun genBackboneSegment(
            numSlices: Int,
            radius: Float,
            position_start: Vector3,
            position_end: Vector3,
            color: FloatArray) {

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = mMol.dcOffset / 3

        var i = 0
//        val j: Int
//        val end_color = color

        // debugging
        //        position_start.x = 0;
        //        position_start.y = 0;
        //        position_start.z = 0;
        //
        //        position_end.x = 0;
        //        position_end.y = 1;
        //        position_end.z = 0;

        /*
         * calculate two vectors that are normal to our backbone segment - R and S.
         */
        val p1p2 = Vector3()
        p1p2.setAll(position_end)
        p1p2.subtract(position_start)
         val P = Vector3(Math.random(), Math.random(), Math.random())
        val R = Vector3(p1p2)
        R.cross(P)
        val S = Vector3(R)
        S.cross(p1p2)
        R.normalize()
        S.normalize()

        var x1: Double
        var y1: Double
        var z1: Double
        var x2: Double
        var y2: Double
        var z2: Double

        // val angleStep = 2.0f * Math.PI.toFloat() / numSlices

        /*
         * math: two tris per slice, wrapping for numSlices+1 (hit original again to close)
         *    doubled for two colors on each half
         */
        floatData = FloatArray(6 * (numSlices + 1) * STRIDE_IN_FLOATS)
        mOffset = 0

        /*
         * first generate the points, then map them to the position
         * (1) version 1
         * (2) version 2 - should transform the plane
         * (3) version 3 - based on a spline calculation
         */

        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var n: FloatArray
        while (i <= numSlices) {

            // TODO: fix number of slices and generate sin/cos lookup table

            val angleInRadians1 = i.toDouble() / numSlices.toDouble() * (Math.PI * 2f)
            val angleInRadians2 = (i + 1).toFloat() / numSlices.toFloat() * (Math.PI.toFloat() * 2f)

            val s1 = radius * MathUtil.sin(angleInRadians1)
            val s2 = radius * MathUtil.sin(angleInRadians2.toDouble())
            val c1 = radius * MathUtil.cos(angleInRadians1)
            val c2 = radius * MathUtil.cos(angleInRadians2.toDouble())

            /*
             * no mid point
             */

            // first top point

            x1 = R.x * c1 + S.x * s1
            y1 = R.y * c1 + S.y * s1
            z1 = R.z * c1 + S.z * s1

            p1[0] = (x1 + position_end.x).toFloat()
            p1[1] = (y1 + position_end.y).toFloat()
            p1[2] = (z1 + position_end.z).toFloat()


            // first bottom point

            p2[0] = (x1 + position_start.x).toFloat()
            p2[1] = (y1 + position_start.y).toFloat()
            p2[2] = (z1 + position_start.z).toFloat()


            // SECOND BOTTOM point
            x2 = R.x * c2 + S.x * s2
            y2 = R.y * c2 + S.y * s2
            z2 = R.z * c2 + S.z * s2

            p3[0] = (x2 + position_start.x).toFloat()
            p3[1] = (y2 + position_start.y).toFloat()
            p3[2] = (z2 + position_start.z).toFloat()
            // OK that is one triangle.

            n = XYZ.getNormal(p1, p2, p3)
            putTri(p1, p2, p3, n, color)

            // SECOND triangle NOW

            // first top point

            p1[0] = (x1 + position_end.x).toFloat()
            p1[1] = (y1 + position_end.y).toFloat()
            p1[2] = (z1 + position_end.z).toFloat()


            // SECOND BOTTOM point

            p2[0] = (x2 + position_start.x).toFloat()
            p2[1] = (y2 + position_start.y).toFloat()
            p2[2] = (z2 + position_start.z).toFloat()


            // SECOND top point

            x2 = R.x * c2 + S.x * s2
            y2 = R.y * c2 + S.y * s2
            z2 = R.z * c2 + S.z * s2

            p3[0] = (x2 + position_end.x).toFloat()
            p3[1] = (y2 + position_end.y).toFloat()
            p3[2] = (z2 + position_end.z).toFloat()

            

            n = XYZ.getNormal(p1, p2, p3)
            putTri(p1, p2, p3, n, color)
            i++


        }  // end for loop for body

        numIndices = mOffset

        val vertexDataBuffer = ByteBuffer
                .allocateDirect(floatData.size * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        vertexDataBuffer.put(floatData).position(0)

        GLES20.glGenBuffers(1, vbo, 0)

        if (vbo[0] > 0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0])
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * BYTES_PER_FLOAT,
                    vertexDataBuffer, GLES20.GL_STATIC_DRAW)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        } else {
            // errorHandler.handleError(ErrorHandler.ErrorType.BUFFER_CREATION_ERROR, "glGenBuffers");
            throw RuntimeException("error on buffer gen")
        }
    }

    fun render(
            positionAttribute: Int,
            colorAttribute: Int,
            normalAttribute: Int,
            doWireframeRendering: Boolean) {

        GLES20.glDisable(GLES20.GL_CULL_FACE)

        if (vbo[0] > 0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0])
            // associate the attributes with the bound buffer
            GLES20.glVertexAttribPointer(positionAttribute,
                    POSITION_DATA_SIZE_IN_ELEMENTS,
                    GLES20.GL_FLOAT,
                    false,
                    STRIDE_IN_BYTES,
                    0)  // offset
            GLES20.glEnableVertexAttribArray(positionAttribute)

            GLES20.glVertexAttribPointer(normalAttribute, NORMAL_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                    STRIDE_IN_BYTES, POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT)
            GLES20.glEnableVertexAttribArray(normalAttribute)

            GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                    STRIDE_IN_BYTES, (POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS) * BYTES_PER_FLOAT)
            GLES20.glEnableVertexAttribArray(colorAttribute)

            // Draw
            val todo = if (doWireframeRendering) {
                GLES20.GL_LINES
            } else {
                GLES20.GL_TRIANGLES
            }

            GLES20.glDrawArrays(todo, 0, numIndices)

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)  // release
        } else {
            // errorHandler(// do something );
            throw RuntimeException("buffer manager render: null buffer")
        }
        GLES20.glEnable(GLES20.GL_CULL_FACE)
    }

    fun release() {
        if (vbo[0] > 0) {
            GLES20.glDeleteBuffers(vbo.size, vbo, 0)
            vbo[0] = 0
        }
    }

    private fun putTri(p1: FloatArray, p2: FloatArray, p3: FloatArray, n: FloatArray, color: FloatArray) {

        n[0] *= -normal_brightness_factor
        n[1] *= -normal_brightness_factor
        n[2] *= -normal_brightness_factor

        floatData[mOffset++] = p1[0]
        floatData[mOffset++] = p1[1]
        floatData[mOffset++] = p1[2]
        floatData[mOffset++] = n[0]
        floatData[mOffset++] = n[1]
        floatData[mOffset++] = n[2]
        floatData[mOffset++] = color[0]
        floatData[mOffset++] = color[1]
        floatData[mOffset++] = color[2]
        floatData[mOffset++] = color[3]

        floatData[mOffset++] = p2[0]
        floatData[mOffset++] = p2[1]
        floatData[mOffset++] = p2[2]
        floatData[mOffset++] = n[0]
        floatData[mOffset++] = n[1]
        floatData[mOffset++] = n[2]
        floatData[mOffset++] = color[0]
        floatData[mOffset++] = color[1]
        floatData[mOffset++] = color[2]
        floatData[mOffset++] = color[3]

        floatData[mOffset++] = p3[0]
        floatData[mOffset++] = p3[1]
        floatData[mOffset++] = p3[2]
        floatData[mOffset++] = n[0]
        floatData[mOffset++] = n[1]
        floatData[mOffset++] = n[2]
        floatData[mOffset++] = color[0]
        floatData[mOffset++] = color[1]
        floatData[mOffset++] = color[2]
        floatData[mOffset++] = color[3]
    }

    companion object {
        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

        private var normal_brightness_factor = 10f
    }


}