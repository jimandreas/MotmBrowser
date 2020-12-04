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

@file:Suppress("unused")

package com.bammellab.motm

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.bammellab.motm.browse.BrowseFragmentCache
import com.bammellab.motm.util.PrefsUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity :
        AppCompatActivity(),
        LifecycleObserver,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var navView: BottomNavigationView
    private var fragmentCache: BrowseFragmentCache? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        PrefsUtil.managePrefsInBackground()

        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_browse,
                        R.id.navigation_search,
                        R.id.navigation_settings
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
            when (destination.id) {

                R.id.navigation_browse -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                    toolBar.title = getString(R.string.app_title)

                }
                R.id.navigation_search -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                }
                else -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                }
            }
        }



    }

    override fun onDestroy() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        fragmentCache?.deleteAll()
        fragmentCache = null
        super.onDestroy()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Timber.v("shared preference changed, key: $key")
    }

    fun getFragmentCacheHandle(): BrowseFragmentCache {
        if (fragmentCache == null) {
            fragmentCache = BrowseFragmentCache()
            fragmentCache!!.createFragments()
        }

        return fragmentCache!!

    }
}
