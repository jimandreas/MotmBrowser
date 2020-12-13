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

@file:Suppress("UnnecessaryVariable", "SwitchIntDef")

package com.bammellab.motm.browse

import android.content.Context
import android.content.res.Configuration.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bammellab.mollib.data.MotmByCategory
import com.bammellab.mollib.data.MotmByCategory.motmTabLabels
import com.bammellab.motm.databinding.FragmentBrowseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.google.android.material.tabs.TabLayoutMediator


class BrowseFragment : androidx.fragment.app.Fragment() {

    private lateinit var browseViewModel: BrowseViewModel
    private lateinit var binding: FragmentBrowseBinding
    private lateinit var contextb: Context
    private lateinit var tabs : TabLayout
    private lateinit var bammelImageView : ImageView

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(inflater)
        browseViewModel = ViewModelProvider(this).get(BrowseViewModel::class.java)
        binding.viewModel = browseViewModel
        binding.lifecycleOwner = this
        contextb = binding.root.context
        viewPager = binding.viewpager

        tabs = binding.tabs
        tabs.tabMode = MODE_SCROLLABLE
        bammelImageView = binding.imageviewBammellab

        // remove the "Bammellab" image banner when in landscape mode
        when (binding.root.resources.configuration.orientation) {
            ORIENTATION_LANDSCAPE -> bammelImageView.visibility = GONE
            ORIENTATION_PORTRAIT -> bammelImageView.visibility = VISIBLE
            ORIENTATION_UNDEFINED -> bammelImageView.visibility = VISIBLE
        }
        // this fix for re-creating the fragments after rotation is CRITICAL:
        // https://stackoverflow.com/a/63936638/3853712
        viewPager.isSaveEnabled = false
        // END OF FIX
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragmentFlavor(position)
            }

            override fun getItemCount(): Int {
                return 5
            }
        }


        TabLayoutMediator(tabs, viewPager) { tab, position ->
            if (position >= 0 && position < motmTabLabels.size) {
                tab.text = motmTabLabels[position]
            } else {
                tab.text = ""
            }
        }.attach()
       /* TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Card.DECK[position].toString()
        }.attach()*/

        return binding.root
    }


    fun fragmentFlavor(position: Int): Fragment {
        val f = when (position) {
            0 -> wireUpFragment(MotmByCategory.MotmCategoryHealth)
            1 -> wireUpFragment(MotmByCategory.MotmCategoryLife)
            2 -> wireUpFragment(MotmByCategory.MotmCategoryBiotech)
            3 -> wireUpFragment(MotmByCategory.MotmCategoryStructures)
            else -> MotmListFragment()
        }
        return f
    }

    private fun wireUpFragment(section: Array<String>): Fragment {
        val fragment = MotmCategoryFragment()
        fragment.fragmentCategory(section)
        return fragment
    }
}