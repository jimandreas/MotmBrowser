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
    }

    private fun createFragment(section: MotmSection): Fragment {
        val fragment = MotmCategoryFragment()
        fragment.setCategory(section)
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