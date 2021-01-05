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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.bammellab.motm.browse

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bammellab.mollib.data.MotmByCategory.MotmCategoryBiotech
import com.bammellab.mollib.data.MotmByCategory.MotmCategoryHealth
import com.bammellab.mollib.data.MotmByCategory.MotmCategoryLife
import com.bammellab.mollib.data.MotmByCategory.MotmCategoryStructures
import com.bammellab.motm.R
import com.bammellab.motm.databinding.FragmentBrowseBinding
import timber.log.Timber

class BrowseFragmentCache {

    var binding: FragmentBrowseBinding? = null

    data class FragmentEntry(val frag: Fragment, val tag: String)

    val fragList = mutableListOf<FragmentEntry>()

    fun createFragments(cl: ConstraintLayout) {
        Timber.e("creating Fragments")
        var f = FragmentEntry(createFragment(MotmCategoryHealth), "Health and Disease")
        fragList.add(f)
        f = FragmentEntry(createFragment(MotmCategoryLife), "Life")
        fragList.add(f)
        f = FragmentEntry(createFragment(MotmCategoryBiotech), "Biotech/Nanotech")
        fragList.add(f)
        f = FragmentEntry(createFragment(MotmCategoryStructures), "Structures")
        fragList.add(f)
        // all Motm entries - no headers - just a list ordered by the increasing pub date

        f = FragmentEntry(MotmListFragment(cl), "All")
        fragList.add(f)
    }

    fun getFragByTag(tag: String): Fragment {
        val fragmentEntry = fragList.find { it.tag == tag }
        return fragmentEntry!!.frag
    }

    private fun createFragment(section: Array<String>): Fragment {
        val fragment = MotmCategoryFragment()
        fragment.fragmentCategory(section)
        return fragment
    }
    fun hideAll() {
        fragList.map { it.frag.view?.visibility = View.GONE }
    }

    fun showAll() {
        fragList.map { it.frag.view?.visibility = View.VISIBLE }
    }
    fun deleteAll() {
        fragList.clear()
    }
}