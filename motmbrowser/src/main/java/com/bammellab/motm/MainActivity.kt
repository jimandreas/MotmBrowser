/*
* Copyright (C) 2016-2018 James Andreas
*
* From code from various sources (iosched, Sunshine advanced, Cheesesquare):
*
*  Copyright (C) 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License
*/

package com.bammellab.motm

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.bammellab.motm.util.Utility
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private val listener = PageChangeListener()
    private lateinit var viewPager: ViewPager
    lateinit var app: MotmApplication

    fun setSavedTabNumber(tab: Int) {
        app.saveSelectedTabNumber = tab
    }

    private fun getSavedTabNumber(): Int {
        return app.saveSelectedTabNumber
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = this.application as MotmApplication

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu)
            ab.setDisplayHomeAsUpEnabled(true)
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this)

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            val snackbar = Snackbar.make(view, "get more info", Snackbar.LENGTH_LONG)

            snackbar.setAction("View website") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(
                        "https://pdb101.rcsb.org/browse")
                startActivity(intent)
            }
            snackbar.show()
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = MODE_SCROLLABLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sample_actions, menu)
        return true
    }

    @SuppressLint("SwitchIntDef")
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> menu.findItem(R.id.menu_night_mode_system).isChecked = true
            //AppCompatDelegate.MODE_NIGHT_AUTO -> menu.findItem(R.id.menu_night_mode_auto).isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> menu.findItem(R.id.menu_night_mode_night).isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> menu.findItem(R.id.menu_night_mode_day).isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menu_night_mode_system -> setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            R.id.menu_night_mode_day -> setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_night_mode_night -> setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //R.id.menu_night_mode_auto -> setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)

        recreate()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        //val listener = PageChangeListener()

        // all Motm entries - no headers - just a list ordered by the increasing pub date
        adapter.addFragment(MotmListFragment(), "All")

        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_HEALTH), "Health and Disease")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_LIFE), "Life")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_BIOTEC), "Biotech/Nanotech")
        adapter.addFragment(createFragment(MotmSection.FRAG_SECTION_STRUCTURES), "Structures")
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(listener)
    }

    inner class PageChangeListener : ViewPager.OnPageChangeListener {
        private var currentTab = 0
        override fun onPageScrollStateChanged(state: Int) {
            Timber.i("onPageScrollStateChanged %d", state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Timber.i("onPageScrolled")
        }

        override fun onPageSelected(position: Int) {
            Timber.i("onPageSelected %d", position)
            setSavedTabNumber(position)

        }

        internal fun getCurrentSelected(): Int {
            return currentTab
        }
    }

    private fun createFragment(section: MotmSection): Fragment {
        val fragment = MotmCategoryFragment()
        fragment.setCategory(section)
        return fragment
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_help -> Utility.viewUrl(getString(R.string.drawer_help_url), this)
            R.id.nav_about -> Utility.viewUrl(getString(R.string.drawer_info_url), this)
            R.id.nav_whodunnit -> Utility.viewUrl(getString(R.string.drawer_whodunnit_url), this)
            R.id.nav_settings ->
                Toast.makeText(this, "Settings are not implemented yet", Toast.LENGTH_SHORT).show()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        Timber.i("MainActivity: onResume page was %d, set to %d",
                listener.getCurrentSelected(), getSavedTabNumber())
        viewPager.setCurrentItem(getSavedTabNumber(), false)

//        viewPager.setCurrentItem(listener.getCurrentSelected(), false)
//        viewPager.setCurrentItem(3, false)
    }

    internal class Adapter(fm: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
        private val fragments = ArrayList<Fragment>()
        private val fragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitles[position]
        }
    }


}
