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
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName")

package com.bammellab.mollib.objects

import android.annotation.SuppressLint
import android.opengl.GLES20
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

/**
 * Buffer Manager
 *
 *
 * set up as a Singleton class to solve the following problems App-wide:
 *
 * 1) allocates a single vertex array for accumulating triangles
 *
 * 2) allocates a single FloatArray for preparing to copy to GL.
 *
 * 3) Allocates GL buffers
 *
 * 4) Renders the GL buffers
 *
 * for more information on Android recommended Singleton Patterns, as opposed to subclassing
 * the Application class (not recommended practice) - see this link:
 *
 * http://developer.android.com/training/volley/requestqueue.html#singleton
 *
 * and List interface:
 * https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html
 */
object BufferManager {

    private var floatArray: FloatArray = FloatArray(150000)
    private var bufferList: ArrayList<GLArrayEntry> = ArrayList()

    var floatArrayIndex: Int
        get() = currentIndex
        set(indexIntoFloatArray) {
            currentIndex = indexIntoFloatArray
        }

    //    public void resetBuffersForNextUsage() {
    //        GLArrayEntry ae;
    //        sFloatArrayIndex = 0;
    //        sBufferUsed = 0;
    //        sReportedUsage = false;
    //
    //        int i;
    //        int count = 0;
    //        for (i = 0; i < bufferList.size(); i++ ) {
    //            ae = bufferList.get(i);
    //            if (ae.bufferInUse) {
    //                count++;
    //            }
    //            bufferList.get(i).clearBufferInUse();
    //        }
    ////        Timber.w("Dropped " + count + " buffers");
    //    }


    fun resetBuffersForNextUsage() {
        var ae: GLArrayEntry
        currentIndex = 0
        sBufferUsed = 0
        sReportedUsage = false
        //        sErrorReported = false;

        var i = 0

        while (i < bufferList.size) {
            ae = bufferList[i]
            ae.bufferInUse = false
            if (ae.bufferAllocated) {
                GLES20.glDeleteBuffers(1, ae.glBuf, 0)
            }
            bufferList[i].clearBufferInUse()
            i++
        }
        bufferList.clear()
        //        Timber.w("Dropped " + count + " buffers");
    }


    fun getFloatArray(requestedNumFloats: Int): FloatArray {
        sBufferUsed += requestedNumFloats
        if (requestedNumFloats + currentIndex < sFloatArraySize) {
            return floatArray
        }

        /*
         * no space.  have to:
         * 1) copy the current float array into a float buffer
         * 2) allocate a GL buffer
         * 3) copy the float buffer into the GL buffer
         */
        transferToGl()

        /*
         * OK that is done.   Now reset the vertex data float buffer
         */
        currentIndex = 0
        return floatArray
    }

    /*
     * private class to track the allocated GL vertex buffers
     */
    private class GLArrayEntry {
        val glBuf: IntArray = IntArray(1)
        var numVertices: Int = 0
        var bufferAllocated: Boolean = false
        var bufferInUse: Boolean = false

        var nativeFloatBuffer: FloatBuffer? = null

        init {
            bufferAllocated = false
            bufferInUse = false
        }

        fun clearBufferInUse() {
            this.bufferInUse = false
        }
    }

    /*
     * move the current vertex accumulator buffer into the OpenGL ES native buffer heap
     * 1) copy the current float array into a float buffer
     * 2) allocate a GL buffer
     * 3) copy the float buffer into the GL buffer
     */
    fun transferToGl() {
        var ae: GLArrayEntry? = null
        var i = 0
        while (i < bufferList.size) {
            ae = bufferList[i]
            if (!ae.bufferInUse) {
                break
            }
            i++
        }
        if (ae == null || i == bufferList.size) {  // create a record and a buffer
            //            Timber.i("creating buffer " + i);

            try {
                ae = GLArrayEntry()
                // TODO: wrap the FloatBuffer
                ae.bufferAllocated = true
                ae.bufferInUse = true
                ae.nativeFloatBuffer = ByteBuffer
                        .allocateDirect(INITIAL_FLOAT_BUFFER_SIZE * BYTES_PER_FLOAT)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e("EXCEPTION FAILED to allocate buffer, at index: %d, buffer not created", i)
                //                ae = null;
                return
            } catch (outOfMemoryError: OutOfMemoryError) {
                val allocated = (i * INITIAL_FLOAT_BUFFER_SIZE * BYTES_PER_FLOAT / 1024 / 1024).toLong()
                Timber.e("FAILED to allocate buffer, at index: %d, total alloc mbytes %d", i, allocated)
                //                ae = null;
                return
            }

        }
        bufferList.add(ae)
        ae.bufferInUse = true
        ae.nativeFloatBuffer!!.put(floatArray).position(0)
        GLES20.glGenBuffers(1, ae.glBuf, 0)
        ae.numVertices = currentIndex / STRIDE_IN_FLOATS
        val numbytes = currentIndex * BYTES_PER_FLOAT

        if (ae.glBuf[0] > 0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, ae.glBuf[0])
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, numbytes,
                    ae.nativeFloatBuffer, GLES20.GL_STATIC_DRAW)

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        } else {
            val glError: Int = GLES20.glGetError()
            if (glError != GLES20.GL_NO_ERROR) {
                Timber.e("transferToGl, glerror =  %s", glError)
            }
            throw RuntimeException("error on buffer gen")
        }

        // dumpVertexList();
    }


    fun render(
            positionAttribute: Int,
            colorAttribute: Int,
            normalAttribute: Int,
            doWireframeRendering: Boolean) {

        var ae: GLArrayEntry

        if (!sReportedUsage) {

            val bufferEfficiency = sBufferUsed / (bufferList.size * sFloatArraySize).toFloat()
            @SuppressLint("DefaultLocale") val prettyPrint = String.format("%6.2f", bufferEfficiency)

            Timber.i("Buffer used: %d triangles: %d buffer percent used: %s",
                    sBufferUsed, sBufferUsed / STRIDE_IN_FLOATS / 3, prettyPrint)
            sReportedUsage = true
        }

        GLES20.glEnable(GLES20.GL_CULL_FACE)

        for (i in bufferList.indices) {
            ae = bufferList[i]
            if (!ae.bufferAllocated) {
                continue
            }

            if (!ae.bufferInUse) {
                continue
            }
            if (ae.glBuf[0] > 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, ae.glBuf[0])
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

                GLES20.glDrawArrays(todo, 0, ae.numVertices)

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)  // release
            }
            //            else {
            // errorHandler(// do something );
            // Timber.e("buffer manager render: buffer NOT MAPPED at index " + i);
            // Do NOTHING
            //            }
        }

        // GLES20.glEnable(GLES20.GL_CULL_FACE);

    }

    private val sVertexDataFloatBuffer: FloatBuffer? = null
    private var currentIndex: Int = 0
    private var sBufferUsed: Int = 0
    private var sReportedUsage: Boolean = false

    private lateinit var this_instance: BufferManager
    private var initialized: Boolean? = null

    private const val INITIAL_FLOAT_BUFFER_SIZE = 150000
    private const val sFloatArraySize = INITIAL_FLOAT_BUFFER_SIZE
    private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
    private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
    private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4
    private const val BYTES_PER_FLOAT = 4
    //    private static final int BYTES_PER_SHORT = 2;

    private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
    private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

}
