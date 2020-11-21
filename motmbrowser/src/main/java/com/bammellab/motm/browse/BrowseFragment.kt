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

@file:Suppress("UnnecessaryVariable", "IfThenToSafeAccess")

package com.bammellab.motm.browse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bammellab.motm.MainActivity
import com.bammellab.motm.databinding.FragmentBrowseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import timber.log.Timber
import java.util.*


class BrowseFragment : Fragment() {

    private lateinit var browseViewModel: BrowseViewModel
    private lateinit var binding: FragmentBrowseBinding
    private lateinit var bcontext: Context
    private lateinit var tabs : TabLayout
    private lateinit var bfc : BrowseFragmentCache
    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.e("Browse On Create View")
        bfc = (activity as MainActivity).getFragmentCacheHandle()
        if (bfc.binding != null) {
            binding = bfc!!.binding!!
        } else {
            binding = FragmentBrowseBinding.inflate(inflater)
            bfc.binding = binding

            browseViewModel =
                    ViewModelProvider(this).get(BrowseViewModel::class.java)
            binding.viewModel = browseViewModel
            binding.lifecycleOwner = this
            bcontext = binding.root.context

            adapter = Adapter(this.parentFragmentManager)

            adapter.addFragment(bfc.getFragByTag("Health and Disease"), "Health and Disease")
            adapter.addFragment(bfc.getFragByTag("Life"), "Life")
            adapter.addFragment(bfc.getFragByTag("Biotech/Nanotech"), "Biotech/Nanotech")
            adapter.addFragment(bfc.getFragByTag("Structures"), "Structures")
            // all Motm entries - no headers - just a list ordered by the increasing pub date
            adapter.addFragment(bfc.getFragByTag("All"), "All")

            binding.viewpager.adapter = adapter
        }

        tabs = binding.tabs
        tabs.setupWithViewPager(binding.viewpager)
        tabs.tabMode = MODE_SCROLLABLE

        return binding.root
    }

    internal class Adapter(fm: FragmentManager) :
            androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragmentList = ArrayList<Fragment>()
        private val titleList = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }


}