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

import android.opengl.GLES20

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * simple placeholder pointer graphic for use in developing the select atom feature
 */
class Pointer {

    /** Store our model data in a float buffer.  */
    private val mPointerPositions: FloatBuffer
    private val mPointerColors: FloatBuffer
    private val mPointerNormals: FloatBuffer

    /** Size of the position data in elements.  */
    private val mPositionDataSize = 3

    /** Size of the color data in elements.  */
    private val mColorDataSize = 4

    /** Size of the normal data in elements.  */
    private val mNormalDataSize = 3

    // Define points for a pointer.

    // X, Y, Z
    private val pointerPositionData = floatArrayOf(
            // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
            // if the points are counter-clockwise we are looking at the "front". If not we are looking at
            // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
            // usually represent the backside of an object and aren't visible anyways.

            // top pointer
            0f, 0f, 0f, -1f, -1f, 0f, 1f, -1f, 0f,

            // body - two tris
            0.666f, -1f, 0f, -0.666f, -1f, 0f, -0.666f, -2f, 0f,

            0.666f, -1f, 0f, -0.666f, -2f, 0f, 0.666f, -2f, 0f)

    // R, G, B, A
    private val pointerColorData = floatArrayOf(
            // Front face (red)
            1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f)

    // X, Y, Z
    // The normal is used in light calculations and is a vector which points
    // orthogonal to the plane of the surface. For a pointer model, the normals
    // should be orthogonal to the points of each face.
    private val pointerNormalData = floatArrayOf(
            // Front face
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f)

    init {
        // Initialize the buffers.

        mPointerPositions = ByteBuffer.allocateDirect(pointerPositionData.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mPointerPositions.put(pointerPositionData).position(0)

        mPointerColors = ByteBuffer.allocateDirect(pointerColorData.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mPointerColors.put(pointerColorData).position(0)

        for (i in pointerNormalData.indices) {
            pointerNormalData[i] *= 10f
        }

        mPointerNormals = ByteBuffer.allocateDirect(pointerNormalData.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mPointerNormals.put(pointerNormalData).position(0)


    }

    fun setup(x: Float, y: Float, z: Float, scalingFactor: Float) {
        var i = 0
        while (i < pointerPositionData.size) {

            mPointerPositions.put(i, x + pointerPositionData[i] / 10.0f * scalingFactor)
            mPointerPositions.put(i + 1, y + pointerPositionData[i + 1] / 10.0f * scalingFactor)
            mPointerPositions.put(i + 2, z + pointerPositionData[i + 2] / 10.0f * scalingFactor)
            i += 3
            //            mFinalCubePositionData[i] = x + pointerPositionData[i] / 3.0f;
            //            mFinalCubePositionData[i+1] = y + pointerPositionData[i+1] / 3.0f;
            //            mFinalCubePositionData[i+2] = z + pointerPositionData[i+2] / 3.0f;
        }
    }

    fun render(mPositionHandle: Int,
               mColorHandle: Int,
               mNormalHandle: Int,
               doWireframeRendering: Boolean) {

        // Draw
        val todo = if (doWireframeRendering) {
            GLES20.GL_LINES
        } else {
            GLES20.GL_TRIANGLES
        }

        // Pass in the position information
        mPointerPositions.position(0)
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mPointerPositions)

        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Pass in the color information
        mPointerColors.position(0)
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, mPointerColors)

        GLES20.glEnableVertexAttribArray(mColorHandle)

        // Pass in the normal information
        mPointerNormals.position(0)
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false,
                0, mPointerNormals)

        GLES20.glEnableVertexAttribArray(mNormalHandle)

        // Draw the pointer.
        GLES20.glDrawArrays(todo, 0, 9)
    }

    companion object {
        private const val BYTES_PER_FLOAT = 4
    }

}
