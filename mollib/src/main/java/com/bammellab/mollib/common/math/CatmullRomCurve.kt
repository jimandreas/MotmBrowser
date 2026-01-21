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

@file:Suppress("unused", "ReplaceWithOperatorAssignment", "ReplaceWithOperatorAssignment")

package com.bammellab.mollib.common.math

/*
 * Copyright 2013 Dennis Ippel
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

import timber.log.Timber
import java.util.Collections
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * Derived from http://www.cse.unsw.edu.au/~lambert/splines/source.html
 * (busted link now)
 * @author dennis.ippel
 */
class CatmullRomCurve : ICurve.ICurve3D {
    private var mPoints: MutableList<MotmVector3>
    var numPoints: Int = 0
        private set
    private var selectedIndex = -1
    override val currentTangent: MotmVector3
    private val currentPoint: MotmVector3
    private var mCalculateTangents: Boolean = false
    private lateinit var segmentLengths: FloatArray
    private var isClosedCurve: Boolean = false
    private val tempNext = MotmVector3()
    private val tempPrevLen = MotmVector3()
    private val tempPointLen = MotmVector3()

    val points: List<MotmVector3>
        get() = mPoints

    init {
        mPoints = Collections.synchronizedList(CopyOnWriteArrayList())
        currentTangent = MotmVector3()
        currentPoint = MotmVector3()
    }

    fun addPoint(point: MotmVector3) {
        try {
            mPoints.add(point)
            numPoints++
        } catch (e: Exception) {
            Timber.e("ERROR on addPoint")
        } catch (outOfMemoryError: OutOfMemoryError) {
            Timber.e("Oops out of memory error on add point")
        }
    }

    fun getPoint(index: Int): MotmVector3 {
        return mPoints[index]
    }


    override fun calculatePoint(result: MotmVector3, t: Float) {
        if (mCalculateTangents) {
            val prevt = if (t == 0.0f) t + DELTA else t - DELTA
            val nextt = if (t == 1.0f) t - DELTA else t + DELTA
            p(currentTangent, prevt)
            p(tempNext, nextt)
            currentTangent.subtract(tempNext)
            currentTangent.multiply(.5f)
            currentTangent.normalize()
        }

        p(result, t)
    }

    fun selectPoint(point: MotmVector3): Int {
        var minDist = java.lang.Float.MAX_VALUE
        selectedIndex = -1
        for (i in 0 until numPoints) {
            val p = mPoints[i]
            val distance = pow2(p.x - point.x) + pow2(p.y - point.y) + pow2(p.z - point.z)
            if (distance < minDist && distance < EPSILON) {
                minDist = distance
                selectedIndex = i
            }
        }
        return selectedIndex
    }

    override fun setCalculateTangents(calculateTangents: Boolean) {
        this.mCalculateTangents = calculateTangents
    }

    private fun b(i: Int, t: Float): Float {
        when (i) {
            -2 -> return ((-t + 2) * t - 1) * t / 2.0f
            -1 -> return ((3 * t - 5) * t * t + 2) / 2.0f
            0 -> return ((-3 * t + 4) * t + 1) * t / 2.0f
            1 -> return (t - 1) * t * t / 2.0f
        }
        return 0.0f
    }

    private fun p(result: MotmVector3, tIn: Float) {
        var t = tIn
        if (t < 0) t += 1
        val end = if (isClosedCurve) 0 else 3
        val start = if (isClosedCurve) 0 else 2
        var currentIndex = start + floor((if (t == 1.0f) t - DELTA else t) * (numPoints - end)).toInt()
        val tdivnum = t * (numPoints - end) - (currentIndex - start)
        currentPoint.setAll(0.0f, 0.0f, 0.0f)

        if (!isClosedCurve) {
            // Limit the bounds for AccelerateDecelerateInterpolator
            currentIndex = max(currentIndex, 2)
            currentIndex = min(currentIndex, mPoints.size - 2)
        }

        for (j in -2..1) {
            val b = b(j, tdivnum)
            var index = if (isClosedCurve) (currentIndex + j + 1) % numPoints else currentIndex + j
            if (index < 0) index = numPoints - index - 2
            val p = mPoints[index]

            currentPoint.x = currentPoint.x + b * p.x
            currentPoint.y = currentPoint.y + b * p.y
            currentPoint.z = currentPoint.z + b * p.z
        }
        result.setAll(currentPoint)
    }

    private fun pow2(value: Float): Float {
        return value * value
    }

    /*
     * Makes this a closed curve. The first and last control points will become actual points in the curve.
     *
     */
    fun isClosedCurve(closed: Boolean) {
        isClosedCurve = closed
    }


    /*
     * Returns an approximation of the length of the curve. The more segments the more precise the result.
     *
     */
    private fun getLength(segments: Int): Float {
        var totalLength = 0.0f

        segmentLengths = FloatArray(segments + 1)
        segmentLengths[0] = 0.0f
        calculatePoint(tempPrevLen, 0.0f)

        for (i in 1..segments) {
            val t = i.toFloat() / segments.toFloat()
            calculatePoint(tempPointLen, t)
            val dist = tempPrevLen.distanceTo(tempPointLen)
            totalLength += dist
            segmentLengths[i] = dist
            tempPrevLen.setAll(tempPointLen)
        }

        return totalLength
    }

    /**
     * Creates a curve with uniformly distributed points. Please note that this might alter the shape of the curve
     * slightly. The higher the resolution, the more closely it will resemble to original curve. You'd typically want to
     * use this for smooth animations with constant speeds.
     *
     * <pre>
     * `
     * myCurve.reparametrizeForUniformDistribution(myCurve.getPoints().size() * 4);
    ` *
    </pre> *
     *
     * @param resolution
     */
    fun reparametrizeForUniformDistribution(resolution: Int) {
        val curveLength = getLength(resolution * 100)
        // -- get the length between each new point
        val segmentDistance = curveLength / resolution
        val numSegments = segmentLengths.size.toFloat()

        val newPoints = Collections.synchronizedList(CopyOnWriteArrayList<MotmVector3>())
        // -- add first control point
        newPoints.add(mPoints[0])
        // -- add first point
        var point = MotmVector3()
        calculatePoint(point, 0.0f)
        newPoints.add(point)

        var currentLength = 0.0f

        var i = 1
        while (i < numSegments) {
            currentLength += segmentLengths[i]
            if (currentLength >= segmentDistance) {
                point = MotmVector3()
                calculatePoint(point, i.toFloat() / (numSegments - 1))
                newPoints.add(point)
                currentLength = 0.0f
            }
            i++
        }

        // -- add last point
        point = MotmVector3()
        calculatePoint(point, 1.0f)
        newPoints.add(point)
        // -- add last control point
        newPoints.add(mPoints[mPoints.size - 1])

        // -- scale control point 1
        var controlPoint = MotmVector3.subtractAndCreate(mPoints[1], mPoints[0])
        var oldDistance = mPoints[1].distanceTo(mPoints[2])
        var newDistance = newPoints[1].distanceTo(newPoints[2])
        controlPoint.multiply(newDistance / oldDistance)
        newPoints[0] = MotmVector3.subtractAndCreate(mPoints[1], controlPoint)

        // -- scale control point 2
        controlPoint = MotmVector3.subtractAndCreate(mPoints[mPoints.size - 2], mPoints[mPoints.size - 1])
        oldDistance = mPoints[mPoints.size - 2].distanceTo(mPoints[mPoints.size - 3])
        newDistance = newPoints[newPoints.size - 2].distanceTo(newPoints[newPoints.size - 3])
        controlPoint.multiply(newDistance / oldDistance)
        newPoints[newPoints.size - 1] = MotmVector3.subtractAndCreate(mPoints[mPoints.size - 2], controlPoint)

        mPoints = newPoints
        numPoints = mPoints.size
    }

    companion object {

        const val EPSILON = 36f
        const val DELTA = .00001f
    }
}
