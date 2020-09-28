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
    private var mPoints: MutableList<Vector3>
    var numPoints: Int = 0
        private set
    private var mSelectedIndex = -1
    override val currentTangent: Vector3
    private val mCurrentPoint: Vector3
    private var mCalculateTangents: Boolean = false
    private lateinit var mSegmentLengths: DoubleArray
    private var isClosedCurve: Boolean = false
    private val mTempNext = Vector3()
    private val mTempPrevLen = Vector3()
    private val mTempPointLen = Vector3()

    val points: List<Vector3>
        get() = mPoints

    init {
        mPoints = Collections.synchronizedList(CopyOnWriteArrayList())
        currentTangent = Vector3()
        mCurrentPoint = Vector3()
    }

    fun addPoint(point: Vector3) {
        mPoints.add(point)
        numPoints++
    }

    fun getPoint(index: Int): Vector3 {
        return mPoints[index]
    }


    override fun calculatePoint(result: Vector3, t: Double) {
        if (mCalculateTangents) {
            val prevt = if (t == 0.0) t + DELTA else t - DELTA
            val nextt = if (t == 1.0) t - DELTA else t + DELTA
            p(currentTangent, prevt)
            p(mTempNext, nextt)
            currentTangent.subtract(mTempNext)
            currentTangent.multiply(.5)
            currentTangent.normalize()
        }

        p(result, t)
    }

    fun selectPoint(point: Vector3): Int {
        var minDist = java.lang.Double.MAX_VALUE
        mSelectedIndex = -1
        for (i in 0 until numPoints) {
            val p = mPoints[i]
            val distance = pow2(p.x - point.x) + pow2(p.y - point.y) + pow2(p.z - point.z)
            if (distance < minDist && distance < EPSILON) {
                minDist = distance
                mSelectedIndex = i
            }
        }
        return mSelectedIndex
    }

    override fun setCalculateTangents(calculateTangents: Boolean) {
        this.mCalculateTangents = calculateTangents
    }

    private fun b(i: Int, t: Double): Double {
        when (i) {
            -2 -> return ((-t + 2) * t - 1) * t / 2.0
            -1 -> return ((3 * t - 5) * t * t + 2) / 2.0
            0 -> return ((-3 * t + 4) * t + 1) * t / 2.0
            1 -> return (t - 1) * t * t / 2.0
        }
        return 0.0
    }

    private fun p(result: Vector3, tIn: Double) {
        var t = tIn
        if (t < 0) t += 1
        val end = if (isClosedCurve) 0 else 3
        val start = if (isClosedCurve) 0 else 2
        var currentIndex = start + floor((if (t == 1.0) t - DELTA else t) * (numPoints - end)).toInt()
        val tdivnum = t * (numPoints - end) - (currentIndex - start)
        mCurrentPoint.setAll(0.0, 0.0, 0.0)

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

            mCurrentPoint.x = mCurrentPoint.x + b * p.x
            mCurrentPoint.y = mCurrentPoint.y + b * p.y
            mCurrentPoint.z = mCurrentPoint.z + b * p.z
        }
        result.setAll(mCurrentPoint)
    }

    private fun pow2(value: Double): Double {
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
    private fun getLength(segments: Int): Double {
        var totalLength = 0.0

        mSegmentLengths = DoubleArray(segments + 1)
        mSegmentLengths[0] = 0.0
        calculatePoint(mTempPrevLen, 0.0)

        for (i in 1..segments) {
            val t = i.toDouble() / segments.toDouble()
            calculatePoint(mTempPointLen, t)
            val dist = mTempPrevLen.distanceTo(mTempPointLen)
            totalLength += dist
            mSegmentLengths[i] = dist
            mTempPrevLen.setAll(mTempPointLen)
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
        val numSegments = mSegmentLengths.size.toDouble()

        val newPoints = Collections.synchronizedList(CopyOnWriteArrayList<Vector3>())
        // -- add first control point
        newPoints.add(mPoints[0])
        // -- add first point
        var point = Vector3()
        calculatePoint(point, 0.0)
        newPoints.add(point)

        var currentLength = 0.0

        var i = 1
        while (i < numSegments) {
            currentLength += mSegmentLengths[i]
            if (currentLength >= segmentDistance) {
                point = Vector3()
                calculatePoint(point, i.toDouble() / (numSegments - 1))
                newPoints.add(point)
                currentLength = 0.0
            }
            i++
        }

        // -- add last point
        point = Vector3()
        calculatePoint(point, 1.0)
        newPoints.add(point)
        // -- add last control point
        newPoints.add(mPoints[mPoints.size - 1])

        // -- scale control point 1
        var controlPoint = Vector3.subtractAndCreate(mPoints[1], mPoints[0])
        var oldDistance = mPoints[1].distanceTo(mPoints[2])
        var newDistance = newPoints[1].distanceTo(newPoints[2])
        controlPoint.multiply(newDistance / oldDistance)
        newPoints[0] = Vector3.subtractAndCreate(mPoints[1], controlPoint)

        // -- scale control point 2
        controlPoint = Vector3.subtractAndCreate(mPoints[mPoints.size - 2], mPoints[mPoints.size - 1])
        oldDistance = mPoints[mPoints.size - 2].distanceTo(mPoints[mPoints.size - 3])
        newDistance = newPoints[newPoints.size - 2].distanceTo(newPoints[newPoints.size - 3])
        controlPoint.multiply(newDistance / oldDistance)
        newPoints[newPoints.size - 1] = Vector3.subtractAndCreate(mPoints[mPoints.size - 2], controlPoint)

        mPoints = newPoints
        numPoints = mPoints.size
    }

    companion object {

        const val EPSILON = 36
        const val DELTA = .00001
    }
}
