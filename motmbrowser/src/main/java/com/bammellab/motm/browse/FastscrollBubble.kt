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

/*
CLAUDE OPUS 4.5 changes:

  Critical bug fixes:
  1. Fixed line 305 - Changed bottomAddedScrollValue to topAddedScrollValue
  2. Moved static state to instance variables - currentState, bottomAddedScrollValue, topAddedScrollValue are now per-instance
  3. Refactored state logic - Bottom/top region detection now uses mutually exclusive when expression

  Resource management:
  4. Added cleanup in ON_DESTROY - Removes scroll listener and cancels timer

  Safety improvements:
  5. Added bounds checking - All monthList[index] accesses now use coerceIn(0, lastValidIndex)
  6. Made animations consistent - Added .start() to show() and fade() functions

  Code quality:
  7. Replaced magic number - 1.8f multiplier replaced with calculated offset using getLocationOnScreen()
  8. Removed unused variable - Deleted previousY
 */
package com.bammellab.motm.browse

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_ANY
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.R
import com.bammellab.motm.browse.FastscrollBubble.Companion.BOTTOMSTATE.POSITION_IS_AT_BOTTOM
import com.bammellab.motm.browse.FastscrollBubble.Companion.BOTTOMSTATE.POSITION_IS_AT_TOP
import com.bammellab.motm.browse.FastscrollBubble.Companion.BOTTOMSTATE.POSITION_IS_IN_MIDDLE_SECTION
import com.bammellab.motm.util.OneShotTimer
import timber.log.Timber

class FastscrollBubble(
        private val constraintLayout: ConstraintLayout,
        private val recyclerView: RecyclerView,
        private val viewLifecycleOwner: LifecycleOwner,
        private val monthList: Array<String>)
    : LifecycleEventObserver, OneShotTimer.Callback, View.OnTouchListener {

    private lateinit var thumbImageView: ImageView
    private lateinit var t2020: View
    private lateinit var t2015: View
    private lateinit var t2010: View
    private lateinit var t2005: View
    private lateinit var tcurrent: TextView
    var numMonths : Int = monthList.size

    // Instance state (moved from companion object to avoid shared state issues)
    private var currentState = POSITION_IS_IN_MIDDLE_SECTION
    private var bottomAddedScrollValue = 0
    private var topAddedScrollValue = 0

    private lateinit var debugTextView: TextView  // set to VISIBLE for debugging

    private val thumbTimer = OneShotTimer(recyclerView.context)
    private lateinit var scrollListener: ScrollListener

    @SuppressLint("ClickableViewAccessibility")
    fun setup() {
        scrollListener = ScrollListener()
        recyclerView.addOnScrollListener(scrollListener)
        viewLifecycleOwner.lifecycle.addObserver(this)

        thumbImageView = constraintLayout.findViewById(R.id.thumbFastscrollerImageview)
        t2020 = constraintLayout.findViewById(R.id.t2020)
        t2015 = constraintLayout.findViewById(R.id.t2015)
        t2010 = constraintLayout.findViewById(R.id.t2010)
        t2005 = constraintLayout.findViewById(R.id.t2005)
        tcurrent = constraintLayout.findViewById(R.id.tcurrent)
        debugTextView = constraintLayout.findViewById(R.id.debugTextView)

        thumbTimer.setCallback(this)
        thumbImageView.setOnTouchListener(this)

    }

    private fun showDateLine() {
        t2020.show()
        t2015.show()
        t2010.show()
        t2005.show()
        tcurrent.show()
    }

    private fun hideAll() {
        t2020.visibility = View.INVISIBLE
        t2015.visibility = View.INVISIBLE
        t2010.visibility = View.INVISIBLE
        t2005.visibility = View.INVISIBLE
        tcurrent.visibility = View.INVISIBLE
        thumbImageView.visibility = View.INVISIBLE
    }

    private fun View.show() {
        with(this) {
            this.visibility = VISIBLE
            animate().apply {
                duration = 200
                alpha(1.0f)
            }.start()
        }
    }

    private fun fadeOutDates() {
        t2020.fade()
        t2015.fade()
        t2010.fade()
        t2005.fade()
        tcurrent.fade()
    }

    private fun View.fade() {
        with(this) {
            animate().apply {
                duration = 200
                alpha(0.0f)
            }.start()
        }
    }

    private fun slideInAnmationThumb() {
        with(thumbImageView) {
            animate().cancel()
            //translationX = containerWidth.toFloat()+width
            visibility = VISIBLE
            animate().apply {
                duration = 200
                setListener(null)
                translationX(-width.toFloat())
            }.start()
        }
    }

    private fun slideOutAnimationThumb() {
       // val l = ThumbListener()
        with(thumbImageView) {
            animate().apply {
                duration = 500
                translationX(width.toFloat())
               // setListener(l)
                start()
            }
        }
    }

    override fun timerFinished() {
        slideOutAnimationThumb()
        fadeOutDates()
    }

    /**
     * ON_PAUSE and ON_RESUME signal entry and exit to the "ALL" view pager section.
     * Use these signals to control the native recyclerview scroll bar display.
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        when (event) {

            ON_RESUME -> {
                Timber.v("On_RESUME")
                recyclerView.isVerticalScrollBarEnabled = false
            }

            ON_PAUSE -> {
                Timber.v("On_PAUSE")
                recyclerView.isVerticalScrollBarEnabled = true
                hideAll()
            }

            ON_CREATE -> Timber.v("On_CREATE")

            ON_START -> Timber.v("On_START")

            ON_STOP -> Timber.v("On_STOP")

            ON_DESTROY -> {
                Timber.v("On_DESTROY")
                // Clean up resources to avoid leaks
                recyclerView.removeOnScrollListener(scrollListener)
                thumbTimer.cancelTimer()
            }

            ON_ANY -> Timber.v("On_ANY")
        }
    }

/*    inner class ThumbListener : Animator.AnimatorListener {

        override fun onAnimationEnd(animation: Animator?) {
            // Timber.e("ON END")
            thumbImageView.visibility = View.GONE
        }

        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }*/

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //Timber.v("OnScrollChanged newState = $newState")
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    slideInAnmationThumb()
                    //Timber.v("Dragging")
                    if (!thumbTimer.isRunning) {
                        thumbTimer.startTimer(3000)
                    } else {
                        thumbTimer.setTimerValue(3000)
                    }
                }
//                RecyclerView.SCROLL_STATE_IDLE -> Timber.v("Idle")
//                RecyclerView.SCROLL_STATE_SETTLING -> Timber.v("Settling")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val last = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (first < 0 || last < 0) {
                return
            }
            var ave = (first + last).toFloat() / 2f
            if (ave < 0f) {
                Timber.e("ave is $ave, first $first last $last")
            }
            if (ave >= monthList.size) {
                ave = (monthList.size - 1).toFloat()
            }

            thumbTimer.setTimerValue(3000)

            val curPos = ave * (recyclerView.height.toFloat() - thumbImageView.height) / numMonths.toFloat()


            val bottomDelta: Int = recyclerView.height - curPos.toInt()
            // Timber.e("first = $first last = $last curPos $curPos bDelta $bottomDelta tHeight ${thumbImageView.height}")
            // handle bottom edge condition:

            if (bottomDelta > thumbImageView.height) {
                thumbImageView.y = curPos
                tcurrent.y = curPos
            }

            val unclippedPos = ave * recyclerView.height.toFloat() / numMonths.toFloat()


            /*
             *  Region handling - the thumb must stop at boundaries but scrolling
             *  continues to show correct values. Uses mutually exclusive state transitions.
             */
            var str: String
            val bottomThreshold = recyclerView.height.toFloat() - thumbImageView.height.toFloat()
            val topThreshold = thumbImageView.height.toFloat()

            // Determine new state based on position (mutually exclusive)
            val newState = when {
                unclippedPos > bottomThreshold -> POSITION_IS_AT_BOTTOM
                unclippedPos < topThreshold -> POSITION_IS_AT_TOP
                else -> POSITION_IS_IN_MIDDLE_SECTION
            }

            // Reset scroll value counters when entering a boundary region
            if (newState != currentState) {
                when (newState) {
                    POSITION_IS_AT_BOTTOM -> bottomAddedScrollValue = 0
                    POSITION_IS_AT_TOP -> topAddedScrollValue = 0
                    POSITION_IS_IN_MIDDLE_SECTION -> {
                        bottomAddedScrollValue = 0
                        topAddedScrollValue = 0
                    }
                }
                currentState = newState
            }

            // Set display string based on current state (with bounds checking)
            val lastValidIndex = monthList.size - 1
            when (currentState) {
                POSITION_IS_IN_MIDDLE_SECTION -> {
                    val index = ave.toInt().coerceIn(0, lastValidIndex)
                    str = monthList[index]
                }
                POSITION_IS_AT_BOTTOM -> {
                    bottomAddedScrollValue += 1
                    val index = if (ave.toInt() + bottomAddedScrollValue < last - 1) {
                        (ave.toInt() + bottomAddedScrollValue).coerceIn(0, lastValidIndex)
                    } else {
                        (last - 1).coerceIn(0, lastValidIndex)
                    }
                    str = monthList[index]
                }
                POSITION_IS_AT_TOP -> {
                    topAddedScrollValue += 1
                    val index = if (ave.toInt() - topAddedScrollValue > 1) {
                        (ave.toInt() - topAddedScrollValue).coerceIn(0, lastValidIndex)
                    } else {
                        0
                    }
                    str = monthList[index]
                }
            }

            tcurrent.text = str
        }
    }

    override fun onTouch(v: View?, m: MotionEvent?): Boolean {
        v?.performClick()  // this does nothing but lint likes it

        val rHeight = recyclerView.height.toFloat()
        val tHeight = thumbImageView.height.toFloat()

        if (m == null) return true
        val action = m.actionMasked
        when (action) {
            ACTION_DOWN -> {
                showDateLine()
                thumbTimer.setTimerValue(3000)
            }
            ACTION_MOVE -> {
                thumbTimer.setTimerValue(3000)
                // Calculate offset based on RecyclerView's actual screen position
                val location = IntArray(2)
                recyclerView.getLocationOnScreen(location)
                val recyclerViewTop = location[1].toFloat()
                val touchOffset = recyclerViewTop + tHeight / 2f
                val pos = (m.rawY - touchOffset) * numMonths.toFloat() / rHeight
                debugTextView.text = pos.toString()
                // Bias 5 elements higher to compensate for visual offset
                val biasedPos = pos.toInt() - 5
                val clampedPos = biasedPos.coerceIn(0, numMonths - 1)
                recyclerView.scrollToPosition(clampedPos)
            }
        }
        return false
    }

/*    private fun clipY() {
        if (thumbImageView.y < 0) {
            thumbImageView.y = 0f
        }
        if (thumbImageView.y > recyclerView.height - 2*thumbImageView.height) {
            thumbImageView.y = recyclerView.height - 2*thumbImageView.height.toFloat()
        }
    }*/

    companion object {
        enum class BOTTOMSTATE {
            POSITION_IS_AT_TOP,
            POSITION_IS_IN_MIDDLE_SECTION,
            POSITION_IS_AT_BOTTOM
        }
    }


}