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

@file:Suppress("NOTHING_TO_INLINE")

package com.bammellab.mollib.common.math

object FastSinCos {
    private const val PRECISION = 0x020000
    private const val PI = Math.PI.toFloat()
    private const val TWO_PI = PI * 2
    const val HALF_PI = (PI * .5).toFloat()

    private const val RAD_SLICE = TWO_PI / PRECISION
    const val PRECISION_DIV_2PI = PRECISION / TWO_PI
    const val PRECISION_S = PRECISION - 1
    val sinTable = FloatArray(PRECISION)

    init {
        var rad: Float
        for (i in 0 until PRECISION) {
            rad = i * RAD_SLICE
            sinTable[i] = kotlin.math.sin(rad)
        }
    }

    inline fun sin(radians: Float): Float {
        return sinTable[((radians * PRECISION_DIV_2PI).toInt() and PRECISION_S)]
    }

    inline fun cos(radians: Float): Float {
        return sinTable[(((HALF_PI - radians) * PRECISION_DIV_2PI).toInt() and PRECISION_S)]
    }
}