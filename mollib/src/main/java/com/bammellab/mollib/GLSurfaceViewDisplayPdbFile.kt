/*
 *  Copyright 2023 Bammellab / James Andreas
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

@file:Suppress("unused", "unused_variable", "unused_parameter", "deprecation")

package com.bammellab.mollib

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.widget.Scroller
import com.bammellab.mollib.RendererDisplayPdbFile.Companion.SHADER_PER_PIXEL
import com.bammellab.mollib.RendererDisplayPdbFile.Companion.SHADER_POINT_NO_NORMALS
import com.bammellab.mollib.objects.BufferManager
import kotlin.math.atan2
import kotlin.math.sqrt

@Suppress("ConstantConditionIf")
class GLSurfaceViewDisplayPdbFile : GLSurfaceView {
    var selectMode = false
    private var lastTouchState = NO_FINGER_DOWN

    private lateinit var renderer: RendererDisplayPdbFile

    private var scroller: Scroller? = null
    private var gestureDetector: GestureDetector? = null
    private var mContext: Context? = null

    // Offsets for touch events
    private var previousX  = 0f
    private var previousY = 0f
    private var density = 0f
    private var initialSpacing = 0f

    private var oldX = 0f
    private var oldY = 0f

    private val isAnimationRunning: Boolean
        get() = !scroller!!.isFinished

    constructor(contextIn: Context) : super(contextIn) {
        init(contextIn)
    }

    constructor(contextIn: Context, attrs: AttributeSet) : super(contextIn, attrs) {
        init(contextIn)
    }

    private fun init(contextIn: Context) {

        mContext = contextIn
        scroller = Scroller(contextIn, null, true)

        // The scroller doesn't have any built-in animation functions--it just supplies
        // values when we ask it to. So we have to have a way to call it every frame
        // until the fling ends. This code (ab)uses a ValueAnimator object to generate
        // a callback on every animation frame. We don't use the animated value at all.

        val scrollAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            start()
        }
        scrollAnimator.addUpdateListener {
            // tickScrollAnimation();
        }
        // Create a gesture detector to handle onTouch messages
        gestureDetector = GestureDetector(contextIn, GestureListener())

        // Turn off long press--this control doesn't use it, and if long press is enabled,
        // you can't scroll for a bit, pause, then scroll some more (the pause is interpreted
        // as a long press, apparently)
        gestureDetector!!.setIsLongpressEnabled(false)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        super.surfaceCreated(holder)
        renderMode = RENDERMODE_WHEN_DIRTY

    }

    // with h/t to :

    // http://stackoverflow.com/questions/14818530/how-to-implement-a-two-finger-drag-gesture-on-android
    // and:
    // http://judepereira.com/blog/multi-touch-in-android-translate-scale-and-rotate/

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(m: MotionEvent?): Boolean {
        val x1: Float
        val x2: Float
        val y1: Float
        val y2: Float
        var deltax: Float
        var deltay: Float
        var deltaSpacing: Float


        if (m == null) {
            return true
        }

        // hand the event to the GestureDetector
        // ignore the result for now.
        // TODO:  hook up fling logic
        val result = gestureDetector!!.onTouchEvent(m)

        renderMode = RENDERMODE_CONTINUOUSLY
        if (BufferManager.isBigMolecule) {
            renderer.overrideShader(SHADER_POINT_NO_NORMALS)
        }
        BufferManager.renderLineDuringTouchProcessing(true)

        if (m.actionMasked == ACTION_UP) {
            renderMode = RENDERMODE_WHEN_DIRTY
            BufferManager.renderLineDuringTouchProcessing(false)
            renderer.overrideShader(SHADER_PER_PIXEL)
            requestRender()
        }
        //Number of touches
        val pointerCount = m.pointerCount
        when {
            pointerCount > 2 -> {
                lastTouchState = MORE_FINGERS
                return true
            }
            pointerCount == 2 -> {
                if (selectMode) return true
                val action = m.actionMasked
                val actionIndex = m.actionIndex
                if (lastTouchState == MORE_FINGERS) {
                    x1 = m.getX(0)
                    y1 = m.getY(0)
                    x2 = m.getX(1)
                    y2 = m.getY(1)

                    renderer.touchX = m.x
                    renderer.touchY = m.y

                    oldX = (x1 + x2) / 2.0f
                    oldY = (y1 + y2) / 2.0f
                    lastTouchState = TWO_FINGERS_DOWN
                    return true
                }
                when (action) {
                    MotionEvent.ACTION_MOVE -> {

                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        renderer.touchX = m.x
                        renderer.touchY = m.y

                        deltax = (x1 + x2) / 2.0f
                        deltax -= oldX
                        deltay = (y1 + y2) / 2.0f
                        deltay -= oldY

                        renderer.deltaTranslateX = renderer.deltaTranslateX + deltax / (density * MOVE_SCALE_FACTOR)
                        renderer.deltaTranslateY = renderer.deltaTranslateY - deltay / (density * MOVE_SCALE_FACTOR)

                        oldX = (x1 + x2) / 2.0f
                        oldY = (y1 + y2) / 2.0f

                        val currentSpacing = spacing(m)

                        if (lastTouchState != TWO_FINGERS_DOWN) {
                            initialSpacing = spacing(m)
                        } else {
                            deltaSpacing = currentSpacing - initialSpacing
                            deltaSpacing /= initialSpacing


                            // TODO: adjust this exponent.
                            //   for now, hack into buckets
                            if (renderer.scaleCurrentF < 0.1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 1000f
                            } else if (renderer.scaleCurrentF < 0.1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 500f
                            } else if (renderer.scaleCurrentF < 0.5f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 200f
                            } else if (renderer.scaleCurrentF < 1f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 50f
                            } else if (renderer.scaleCurrentF < 2f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                            } else if (renderer.scaleCurrentF < 5f) {
                                renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                            } else if (renderer.scaleCurrentF > 5f) {
                                if (deltaSpacing > 0) {
                                    renderer.scaleCurrentF = renderer.scaleCurrentF + -deltaSpacing / 10f
                                }
                            }
                        }
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        x1 = m.getX(0)
                        y1 = m.getY(0)
                        x2 = m.getX(1)
                        y2 = m.getY(1)

                        renderer.touchX = m.x
                        renderer.touchY = m.y

                        oldX = (x1 + x2) / 2.0f
                        oldY = (y1 + y2) / 2.0f
                        initialSpacing = spacing(m)
                    }
                    MotionEvent.ACTION_POINTER_UP -> renderMode = RENDERMODE_WHEN_DIRTY
                }
                lastTouchState = TWO_FINGERS_DOWN
                return true
            }
            pointerCount == 1 -> {
                /*
                 * rotate
                 */
                val x = m.x
                val y = m.y

                renderer.touchX = m.x
                renderer.touchY = m.y

                if (m.action == MotionEvent.ACTION_MOVE) {
                    if (lastTouchState != ONE_FINGER_DOWN) {  // handle anything to one finger interaction
                        lastTouchState = ONE_FINGER_DOWN
                    } else {
                        val deltaX = (x - previousX) / density * ROTATION_SCALE_FACTOR
                        val deltaY = (y - previousY) / density * ROTATION_SCALE_FACTOR

                        renderer.deltaX = renderer.deltaX + deltaX
                        renderer.deltaY = renderer.deltaY + deltaY
                    }
                }
                previousX = x
                previousY = y

                return true
            }

        }

        return super.onTouchEvent(m)
    }

    /**
     * Determine the space between the first two fingers
     */
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    /*
     * Calculate the degree to be rotated by.
     */
    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1))
        val deltaY = (event.getY(0) - event.getY(1))
        val radians = atan2(deltaY, deltaX)
        return Math.toDegrees(radians.toDouble()).toFloat()
    }
    
    fun setRenderer(rendererIn: RendererDisplayPdbFile, densityIn: Int) {
        super.setRenderer(rendererIn)
        renderer = rendererIn
        density = densityIn.toFloat()
        
    }

    /**
     * Extends [GestureDetector.SimpleOnGestureListener] to provide custom gesture
     * processing.
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        /*override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {

            // Timber.w("onScroll");
            return true
        }*/

        // not implemented - probably a bad idea
        //   might be good to average out the pivot to help with jitter
        /*override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {

            // Timber.w("onFling");
            //            // Set up the Scroller for a fling
            //            float scrollTheta = vectorToScalarScroll(
            //                    velocityX,
            //                    velocityY,
            //                    e2.getX() - pieBounds.centerX(),
            //                    e2.getY() - pieBounds.centerY());
            //            mScroller.fling(
            //                    0,
            //                    (int) getPieRotation(),
            //                    0,
            //                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
            //                    0,
            //                    0,
            //                    Integer.MIN_VALUE,
            //                    Integer.MAX_VALUE);
            //
            //            // Start the animator and tell it to animate for the expected duration of the fling.
            //            if (Build.VERSION.SDK_INT >= 11) {
            //                scrollAnimator.setDuration(mScroller.getDuration());
            //                scrollAnimator.start();
            //            }
            return true
        }*/

        override fun onDown(e: MotionEvent): Boolean {

            if (isAnimationRunning) {
                stopScrolling()
            }
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

    /**
     * Force a stop to all pie motion. Called when the user taps during a fling.
     */
    private fun stopScrolling() {
        scroller!!.forceFinished(true)

        onScrollFinished()
    }

    /**
     * Called when the user finishes a scroll action.
     */
    private fun onScrollFinished() {

    }

    companion object {

        private const val NO_FINGER_DOWN = 0
        private const val ONE_FINGER_DOWN = 1
        private const val TWO_FINGERS_DOWN = 2
        private const val MORE_FINGERS = 3

        private const val ROTATION_SCALE_FACTOR = 100f
        private const val MOVE_SCALE_FACTOR = 5f
    }
}