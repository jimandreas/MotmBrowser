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
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName")

package com.bammellab.mollib.objects

import android.opengl.GLES20
import android.os.SystemClock
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

/**
 * Buffer Manager
 *
 * 1) allocates a single vertex array for accumulating triangles
 *
 * 2) allocates a single FloatArray for preparing to copy to GL.
 *
 * 3) Allocates GL buffers
 *
 * 4) Renders the GL buffers
 *
 */

interface ViewModeCallback {
    fun slowRender()
    fun renderQuickLineOnTouch()
}

object BufferManager {

    private var viewModeCallback: ViewModeCallback? = null
    private var floatArray: FloatArray = FloatArray(150000)
    private var bufferList: ArrayList<GLArrayEntry> = ArrayList()
    private val vertexDataFloatBuffer: FloatBuffer? = null
    private var currentIndex = 0
    private var bufferUsedCount = 0
    private var alreadyReportedUsage = false
    private var bufferLoadingComplete = false
    private var outOfMemoryFlag = false


    private lateinit var this_instance: BufferManager

    private var lineModeFlag = false
    fun doLineMode(flag: Boolean) {
        lineModeFlag = flag
    }
    private var touchProcessing = false
    fun renderLineDuringTouchProcessing(flag: Boolean) {
        touchProcessing = flag
    }

    /**
     * this is called by the ManagerViewMode module with a non-null
     * callback pointer when the molecule is large.   This sets the mode
     * for slow rendering
     */
    fun initViewModeCallback(cb: ViewModeCallback?) {
        viewModeCallback = cb
    }

    var floatArrayIndex: Int
        get() = currentIndex
        set(indexIntoFloatArray) {
            currentIndex = indexIntoFloatArray
        }

    fun resetBuffersForNextUsage() {
        var arrayEntry: GLArrayEntry
        currentIndex = 0
        bufferUsedCount = 0
        alreadyReportedUsage = false
        bufferLoadingComplete = false
        outOfMemoryFlag = false

        var i = 0

        while (i < bufferList.size) {
            arrayEntry = bufferList[i]
            arrayEntry.bufferInUse = false
            if (arrayEntry.bufferAllocated) {
                GLES20.glDeleteBuffers(1, arrayEntry.glBuf, 0)
            }
            bufferList[i].clearBufferInUse()
            i++
        }
        bufferList.clear()
    }

    fun setBufferLoadingComplete() {
        bufferLoadingComplete = true
    }


    fun getFloatArray(requestedNumFloats: Int): FloatArray {
        bufferUsedCount += requestedNumFloats
        if (requestedNumFloats + currentIndex < FLOAT_BUFFER_SIZE) {
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
        val glBuf = IntArray(1)
        var numVertices = 0
        var bufferAllocated = false
        var nativeBufferAllocated = false
        var bufferInUse = false

        var nativeFloatBuffer: FloatBuffer? = null

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
        var arrayEntry: GLArrayEntry? = null
        var i = 0
        while (i < bufferList.size) {
            arrayEntry = bufferList[i]
            if (!arrayEntry.bufferInUse) {
                break
            }
            i++
        }
        if (arrayEntry == null || i == bufferList.size) {  // create a record and a buffer
            if (outOfMemoryFlag) {
                return
            }
            try {
                arrayEntry = GLArrayEntry()
                // TODO: wrap the FloatBuffer

                arrayEntry.bufferInUse = true
                if (!arrayEntry.nativeBufferAllocated) {
                    arrayEntry.nativeFloatBuffer = ByteBuffer
                            .allocateDirect(FLOAT_BUFFER_SIZE * BYTES_PER_FLOAT)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                    arrayEntry.nativeBufferAllocated = true
                }

                bufferList.add(arrayEntry)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e("EXCEPTION FAILED to allocate buffer, at index: %d, buffer not created", i)
                //                ae = null;
                return
            } catch (outOfMemoryError: OutOfMemoryError) {
                val allocated = (i * FLOAT_BUFFER_SIZE * BYTES_PER_FLOAT / 1024 / 1024).toLong()
                Timber.e("OOMerror transferToGL, at index: %d, total alloc mbytes %d", i, allocated)
                outOfMemoryFlag = true
                return
            }

        }

        arrayEntry.bufferInUse = true
        arrayEntry.nativeFloatBuffer!!.put(floatArray).position(0)

        if (!arrayEntry.bufferAllocated) {
            arrayEntry.bufferAllocated = true
            GLES20.glGenBuffers(1, arrayEntry.glBuf, 0)
        }
        arrayEntry.numVertices = currentIndex / STRIDE_IN_FLOATS
        val numbytes = currentIndex * BYTES_PER_FLOAT

        if (arrayEntry.glBuf[0] > 0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayEntry.glBuf[0])
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, numbytes,
                    arrayEntry.nativeFloatBuffer, GLES20.GL_STATIC_DRAW)

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        } else {
            val glError: Int = GLES20.glGetError()
            if (glError != GLES20.GL_NO_ERROR) {
                Timber.e("transferToGl, glerror =  %s", glError)
            }
            throw RuntimeException("error on buffer gen")
        }

        // dumpVertexList();

        // if the callback is set, then we are in slow render mode
        //   Call the render function for each buffer load
        if (viewModeCallback != null) {
            bufferLoadingComplete = true
            doTheActualRender(
                    positionAttributeCache,
                    colorAttributeCache,
                    normalAttributeCache,
                    doWireframeRenderingCache)
            // now reset the buffers
            bufferList[0].bufferInUse = false
            //Timber.v("bufferlist size: ${bufferList.size}")
        }
    }

    private var positionAttributeCache = 0
    private var colorAttributeCache = 0
    private var normalAttributeCache = 0
    private var doWireframeRenderingCache = false

    fun render(
            positionAttribute: Int,
            colorAttribute: Int,
            normalAttribute: Int,
            doWireframeRendering: Boolean) {

        if (viewModeCallback != null) {

            val startTime = SystemClock.uptimeMillis().toFloat()

            positionAttributeCache = positionAttribute
            colorAttributeCache = colorAttribute
            normalAttributeCache = normalAttribute
            doWireframeRenderingCache = doWireframeRendering

            // if touch, then do a quick line, otherwise render the whole mol using one buffer
            if (touchProcessing) {
                viewModeCallback!!.renderQuickLineOnTouch()
            } else {
                viewModeCallback!!.slowRender()
            }


            // render the last buffer
            doTheActualRender(positionAttribute, colorAttribute, normalAttribute, doWireframeRendering)

            val endTime = SystemClock.uptimeMillis().toFloat()
            val elapsedTime = (endTime - startTime) / 1000
            val prettyPrint = String.format("%6.2f", elapsedTime)
            Timber.e("slowRender $prettyPrint")

        } else {
            doTheActualRender(positionAttribute, colorAttribute, normalAttribute, doWireframeRendering)
        }
    }

    private fun doTheActualRender(
            positionAttribute: Int,
            colorAttribute: Int,
            normalAttribute: Int,
            doWireframeRendering: Boolean) {

        var arrayEntry: GLArrayEntry

        if (!bufferLoadingComplete) {
            return
        }

//        if (!alreadyReportedUsage) {
//
//            val bufferEfficiency = bufferUsedCount / (bufferList.size * FLOAT_BUFFER_SIZE).toFloat()
//            @SuppressLint("DefaultLocale") val prettyPrint = String.format("%6.2f", bufferEfficiency)
//
//            // Timber.i("Buffer used: %d floats, %d triangles, buffer percent used: %s",
//            //         bufferUsedCount, bufferUsedCount / STRIDE_IN_FLOATS / 3, prettyPrint)
//            alreadyReportedUsage = true
//        }

        GLES20.glEnable(GLES20.GL_CULL_FACE)

        for (i in bufferList.indices) {
            arrayEntry = bufferList[i]
            if (!arrayEntry.bufferAllocated) {
                continue
            }

            if (!arrayEntry.bufferInUse) {
                continue
            }
            if (arrayEntry.glBuf[0] > 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayEntry.glBuf[0])
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
                val todo = if (doWireframeRendering || lineModeFlag) {
                    GLES20.GL_LINES
                } else {
                    GLES20.GL_TRIANGLES
                }

                GLES20.glDrawArrays(todo, 0, arrayEntry.numVertices)

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

    private const val FLOAT_BUFFER_SIZE = 150000
    private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
    private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
    private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4
    private const val BYTES_PER_FLOAT = 4
    private const val STRIDE_IN_FLOATS =
            POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
    private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT


}
