/*
 *  Copyright 2021 Bammellab / James Andreas
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

package com.bammellab.motm.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


/**
 * Then set app:layout_behavior="your.package.KeepWithinParentBoundsScrollingBehavior"
 * on your ViewPager or whatever view you have below the AppBar.
 * Take note that this is not a generic solution for all CoordinatorLayouts,
 * but it seems to work when you have a view below an app bar that you don't
 * want to let extend beyond the bottom of the parent CoordinatorLayout.
 * UPDATE: You should also set app:layout_anchor="@id/app_bar" on your ViewPager
 * for the situation when the keyboard disappears. If you don't the ViewPager
 * layout will not be refreshed when the keyboard disappears and the ViewPager will appear cut off.
 */
// https://stackoverflow.com/a/58296374

class KeepWithinParentBoundsScrollingBehavior : AppBarLayout.ScrollingViewBehavior {

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (dependency !is AppBarLayout) {
            return super.onDependentViewChanged(parent, child, dependency)
        }

        val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.height = parent.height - dependency.bottom
        child.layoutParams = layoutParams
        return super.onDependentViewChanged(parent, child, dependency)
    }
}