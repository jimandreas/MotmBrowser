/*
 *  Copyright 2020 James Andreas
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

@file:Suppress("UnnecessaryVariable")

package com.bammellab.motm.browse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bammellab.motm.databinding.FragmentBrowseBinding
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import java.util.*


class BrowseFragment : Fragment() {

    private lateinit var scanViewModel: BrowseViewModel
    private lateinit var binding: FragmentBrowseBinding
    private lateinit var bcontext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBrowseBinding.inflate(inflater)
        binding.lifecycleOwner = this

        scanViewModel =
            ViewModelProvider(this).get(BrowseViewModel::class.java)
        binding.viewModel = scanViewModel
        bcontext = binding.root.context

        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val adapter = Adapter(this.parentFragmentManager)

        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_HEALTH), "Health and Disease")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_LIFE), "Life")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_BIOTEC), "Biotech/Nanotech")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_STRUCTURES), "Structures")
        // all Motm entries - no headers - just a list ordered by the increasing pub date
        adapter.addFragment(MotmListFragment(), "All")

        binding.viewpager.adapter = adapter

        val tabs = binding.tabs
        tabs.setupWithViewPager(binding.viewpager)
        tabs.tabMode = MODE_SCROLLABLE
    }

    private fun createFragment(section: MotmSection): Fragment {
        val fragment = MotmCategoryFragment()
        fragment.fragmentCategory(section)
        return fragment
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