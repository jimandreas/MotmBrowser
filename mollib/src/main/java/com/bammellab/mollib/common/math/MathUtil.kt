/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2018 Jim Andreas kotlin conversion
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
@file:Suppress("unused")

package com.bammellab.mollib.common.math

import kotlin.math.abs

object MathUtil {
    private const val PRECISION = 0x020000
    const val PI = Math.PI.toFloat()
    const val TWO_PI = PI * 2
    private const val HALF_PI = (PI * .5).toFloat()
    private const val PRE_PI_DIV_180 = PI / 180
    private const val PRE_180_DIV_PI = 180 / PI

    private const val RAD_SLICE = TWO_PI / PRECISION
    private const val PRECISION_DIV_2PI = PRECISION / TWO_PI
    private const val PRECISION_S = PRECISION - 1
    private val sinTable = FloatArray(PRECISION)
    private val tanTable = FloatArray(PRECISION)
    private val isInitialized = initialize()

    private fun initialize(): Boolean {
        var rad: Float
        for (i in 0 until PRECISION) {
            rad = i * RAD_SLICE
            sinTable[i] = kotlin.math.sin(rad)
            tanTable[i] = kotlin.math.tan(rad)
        }
        return true
    }

    private fun radToIndex(radians: Float): Int {
        return (radians * PRECISION_DIV_2PI).toInt() and PRECISION_S
    }

    fun sin(radians: Float): Float {
        return sinTable[radToIndex(radians)]
    }

    fun cos(radians: Float): Float {
        return sinTable[radToIndex(HALF_PI - radians)]
    }

    fun tan(radians: Float): Float {
        return tanTable[radToIndex(radians)]
    }

    fun degreesToRadians(degrees: Float): Float {
        return degrees * PRE_PI_DIV_180
    }

    fun radiansToDegrees(radians: Float): Float {
        return radians * PRE_180_DIV_PI
    }

    fun realEqual(a: Float, b: Float, tolerance: Float): Boolean {
        return abs(b - a) <= tolerance
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value
    }

    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else if (value > max) max else value
    }

    fun clamp(value: Short, min: Short, max: Short): Short {
        return if (value < min) min else if (value > max) max else value
    }

    fun getClosestPowerOfTwo(xIn: Int): Int {
        var x = xIn
        --x
        x = x or (x shr 1)
        x = x or (x shr 2)
        x = x or (x shr 4)
        x = x or (x shr 8)
        x = x or (x shr 16)
        return ++x
    }

    const val PIFLOAT = 3.14159265358979323846f
}
