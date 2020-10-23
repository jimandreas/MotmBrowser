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

import com.bammellab.mollib.common.math.MathUtil
import com.bammellab.mollib.common.math.MotmVector3
import com.bammellab.mollib.objects.GlobalObject.BRIGHTNESS_FACTOR
import com.kotmol.pdbParser.AtomInformationTable
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.PdbAtom

class SegmentAtomToAtomBond(private val molecule: Molecule) {

    private lateinit var mVertexData: FloatArray
    private var mOffset: Int = 0

    fun genBondCylinders(
            numSlices: Int,
            radius: Float,
            atom1: PdbAtom,
            atom2: PdbAtom) {

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = (molecule.maxPostCenteringVectorMagnitude / BRIGHTNESS_FACTOR).toFloat()

        var i = 0
        val startColor: FloatArray
        val endColor: FloatArray
        /* start is atom1, end is atom2 */
        val positionStart = MotmVector3(atom1.atomPosition)
        val positionEnd = MotmVector3(atom2.atomPosition)
        val positionMid = MotmVector3()

        positionMid.x = (positionEnd.x + positionStart.x) / 2.0
        positionMid.y = (positionEnd.y + positionStart.y) / 2.0
        positionMid.z = (positionEnd.z + positionStart.z) / 2.0

        var elementSymbol: String = atom1.elementSymbol
        var ai = AtomInformationTable.atomSymboltoAtomNumNameColor[elementSymbol]
        startColor = ai?.color ?: floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // white for unknown
        elementSymbol = atom2.elementSymbol
        ai = AtomInformationTable.atomSymboltoAtomNumNameColor[elementSymbol]
        endColor = ai?.color ?: floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // white for unknown


        // debugging
        //        position_start.x = 0;
        //        position_start.y = 0;
        //        position_start.z = 0;
        //
        //        position_end.x = 0;
        //        position_end.y = 1;
        //        position_end.z = 0;

        /*
         * calculate two vectors that are normal to our bond segment - R and S.
         */
        //        MotmVector3 p1p2 = new MotmVector3();
        //        p1p2.setAll(position_end);
        //        p1p2.subtract(position_start);
        //        MotmVector3 P = new MotmVector3(Math.random(), Math.random(), Math.random());
        //        MotmVector3 R = new MotmVector3(p1p2);
        //        R.cross(P);
        //        MotmVector3 S = new MotmVector3(R);
        //        S.cross(p1p2);
        //        R.normalize();
        //        S.normalize();

        p1p2.setAll(positionEnd)
        p1p2.subtract(positionStart)
        pvec.x = Math.random()
        pvec.y = Math.random()
        pvec.z = Math.random()
        // MotmVector3 R = new MotmVector3(p1p2);
        rvec.setAll(p1p2)
        rvec.cross(pvec)
        // MotmVector3 S = new MotmVector3(R);
        svec.setAll(rvec)
        // S.cross(p1p2);
        svec.cross(p1p2)
        //        R.normalize();
        //        S.normalize();
        rvec.normalize()
        svec.normalize()

        var x1: Double
        var y1: Double
        var z1: Double
        var x2: Double
        var y2: Double
        var z2: Double

        /*
         * math: two tris per slice, wrapping for numSlices+1 (hit original again to close)
         *    doubled for two colors on each half
         */
        mVertexData = BufferManager.getFloatArray(2 * 6 * (numSlices + 1) * STRIDE_IN_FLOATS)
        mOffset = BufferManager.floatArrayIndex

        /*
         * first generate the points, then map them to the position
         * (1) version 1
         * (2) version 2 - should transform the plane
         * (3) version 3 - based on a spline calculation
         */

        //        float mMol.p1[] = new float[3];
        //        float mMol.p2[] = new float[3];
        //        float mMol.p3[] = new float[3];
        var n: FloatArray
        while (i <= numSlices) {

            val angleInRadians1 = (i.toDouble() / numSlices.toDouble()
                    * Math.PI * 2.0)
            val angleInRadians2 = ((i + 1).toDouble() / numSlices.toDouble()
                    * Math.PI * 2.0)

            val s1 = radius * MathUtil.sin(angleInRadians1)
            val s2 = radius * MathUtil.sin(angleInRadians2)
            val c1 = radius * MathUtil.cos(angleInRadians1)
            val c2 = radius * MathUtil.cos(angleInRadians2)

            /*
             * first the START to MID portion of the bond
             */

            // first top point

            x1 = rvec.x * c1 + svec.x * s1
            y1 = rvec.y * c1 + svec.y * s1
            z1 = rvec.z * c1 + svec.z * s1

            p1[0] = (x1 + positionMid.x).toFloat()
            p1[1] = (y1 + positionMid.y).toFloat()
            p1[2] = (z1 + positionMid.z).toFloat()


            // first bottom point

            p2[0] = (x1 + positionStart.x).toFloat()
            p2[1] = (y1 + positionStart.y).toFloat()
            p2[2] = (z1 + positionStart.z).toFloat()


            // SECOND BOTTOM point
            x2 = rvec.x * c2 + svec.x * s2
            y2 = rvec.y * c2 + svec.y * s2
            z2 = rvec.z * c2 + svec.z * s2

            p3[0] = (x2 + positionStart.x).toFloat()
            p3[1] = (y2 + positionStart.y).toFloat()
            p3[2] = (z2 + positionStart.z).toFloat()
            // OK that is one triangle.

            n = XYZ.getNormal(p1, p2, p3)
            putTri(n, startColor)

            // SECOND triangle NOW

            // first top point

            p1[0] = (x1 + positionMid.x).toFloat()
            p1[1] = (y1 + positionMid.y).toFloat()
            p1[2] = (z1 + positionMid.z).toFloat()


            // SECOND BOTTOM point

            p2[0] = (x2 + positionStart.x).toFloat()
            p2[1] = (y2 + positionStart.y).toFloat()
            p2[2] = (z2 + positionStart.z).toFloat()


            // SECOND top point

            x2 = rvec.x * c2 + svec.x * s2
            y2 = rvec.y * c2 + svec.y * s2
            z2 = rvec.z * c2 + svec.z * s2

            p3[0] = (x2 + positionMid.x).toFloat()
            p3[1] = (y2 + positionMid.y).toFloat()
            p3[2] = (z2 + positionMid.z).toFloat()



            n = XYZ.getNormal(p1, p2, p3)
            putTri(n, startColor)

            /*
             * now the ending 1/2 of the bond - from MID to END
             */

            // first top point

            x1 = rvec.x * c1 + svec.x * s1
            y1 = rvec.y * c1 + svec.y * s1
            z1 = rvec.z * c1 + svec.z * s1

            p1[0] = (x1 + positionEnd.x).toFloat()
            p1[1] = (y1 + positionEnd.y).toFloat()
            p1[2] = (z1 + positionEnd.z).toFloat()


            // first bottom point

            p2[0] = (x1 + positionMid.x).toFloat()
            p2[1] = (y1 + positionMid.y).toFloat()
            p2[2] = (z1 + positionMid.z).toFloat()


            // SECOND BOTTOM point
            x2 = rvec.x * c2 + svec.x * s2
            y2 = rvec.y * c2 + svec.y * s2
            z2 = rvec.z * c2 + svec.z * s2

            p3[0] = (x2 + positionMid.x).toFloat()
            p3[1] = (y2 + positionMid.y).toFloat()
            p3[2] = (z2 + positionMid.z).toFloat()
            // OK that is one triangle.

            n = XYZ.getNormal(p1, p2, p3)
            putTri(n, endColor)

            // SECOND triangle NOW

            // first top point

            p1[0] = (x1 + positionEnd.x).toFloat()
            p1[1] = (y1 + positionEnd.y).toFloat()
            p1[2] = (z1 + positionEnd.z).toFloat()


            // SECOND BOTTOM point

            p2[0] = (x2 + positionMid.x).toFloat()
            p2[1] = (y2 + positionMid.y).toFloat()
            p2[2] = (z2 + positionMid.z).toFloat()

            // SECOND top point

            x2 = rvec.x * c2 + svec.x * s2
            y2 = rvec.y * c2 + svec.y * s2
            z2 = rvec.z * c2 + svec.z * s2

            p3[0] = (x2 + positionEnd.x).toFloat()
            p3[1] = (y2 + positionEnd.y).toFloat()
            p3[2] = (z2 + positionEnd.z).toFloat()



            n = XYZ.getNormal(p1, p2, p3)
            putTri(n, endColor)
            i++

        }  // end for loop for body

        //BufferManager.setFloatArrayIndex(mOffset);
        BufferManager.floatArrayIndex = mOffset
    }

    private fun putTri(n: FloatArray, color: FloatArray) {

        n[0] *= normal_brightness_factor
        n[1] *= normal_brightness_factor
        n[2] *= normal_brightness_factor

        mVertexData[mOffset++] = p1[0]
        mVertexData[mOffset++] = p1[1]
        mVertexData[mOffset++] = p1[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = p2[0]
        mVertexData[mOffset++] = p2[1]
        mVertexData[mOffset++] = p2[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = p3[0]
        mVertexData[mOffset++] = p3[1]
        mVertexData[mOffset++] = p3[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]
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

        val p1p2 = MotmVector3()
        val pvec = MotmVector3()
        val rvec = MotmVector3()
        val svec = MotmVector3()
        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var cache2_valid: Boolean = false
    }
}


/*
         * DEBUG:
         * optional vertex printout
         */
//        float nvx, nvy, nvz;
//
//        for (i = 0; i < offset; i += STRIDE_IN_FLOATS) {
//            vx = vertexData[i + 0];
//            vy = vertexData[i + 1];
//            vz = vertexData[i + 2];
//            String svx = String.format("%6.2f", vx);
//            String svy = String.format("%6.2f", vy);
//            String svz = String.format("%6.2f", vz);
//
//            nvx = vertexData[i + 3];
//            nvy = vertexData[i + 4];
//            nvz = vertexData[i + 5];
//            String snvx = String.format("%6.2f", nvx);
//            String snvy = String.format("%6.2f", nvy);
//            String snvz = String.format("%6.2f", nvz);
//
//            Log.w("vert ", i + " x y z nx ny nz "
//                            + svx + " " + svy + " " + svz + " and " + snvx + " " + snvy + " " + snvz
////                    + " clr "
////                    + vertexData[i + 6] + " " + vertexData[i + 7] + " "
////                    + vertexData[i + 8] + " " + vertexData[i + 8]
//            );
//        }


//        FloatBuffer vertexDataBuffer = ByteBuffer
//                .allocateDirect(offset /* intead of the buffer length */ * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        // public FloatBuffer put(float[] src, int srcOffset, int floatCount) {
//        vertexDataBuffer
//                .put(vertexData, 0, offset)
//                .position(0);
//
//        GLES20.glGenBuffers(1, vboBody, 0);
//
//        if (vboBody[0] > 0) {
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboBody[0]);
//            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * BYTES_PER_FLOAT,
//                    vertexDataBuffer, GLES20.GL_STATIC_DRAW);
//
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//        } else {
//            // errorHandler.handleError(ErrorHandler.ErrorType.BUFFER_CREATION_ERROR, "glGenBuffers");
//            throw new RuntimeException("error on buffer gen");
//        }
//        vertexDataBuffer.limit(0);