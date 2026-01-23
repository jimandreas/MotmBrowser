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

package com.bammellab.motm.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.bammellab.motm.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

object PrefsUtil {
    var prefsContext: Context? = null
    var prefsPreviousSearchesSet = mutableSetOf("")
    var prefsThemeCurrentThemeSetting: String = ""
    var prefsTouchToOpenSetting: Boolean = true

    /*
    val previousSet = PrefsUtil.getStringSet(
                PrefsUtil.PREVIOUS_SEARCHES_KEY,
                emptySet())
     */

    private val scope = MainScope()
    private var prefs : SharedPreferences? = null
    private var themeEntryValues: Array<String> = arrayOf("")

    fun managePrefsInBackground() = scope.launch {
        initPreferences()
    }

    /*
     * Do the following on an IO coroutine:
     *  - Register the preference change listener
     *  - Pull current values for the preferences
     *  - Set up the theme
     */
    suspend fun initPreferences() {
        if (prefsContext == null) return
        withContext(Dispatchers.IO) {
            prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext!!)

            updateTheme(prefs, prefsContext!!.resources)

            // pull previous search set
            prefsPreviousSearchesSet = prefs?.getStringSet(PREVIOUS_SEARCHES_KEY, emptySet())!!.toMutableSet()
            // pull touch to open status
            prefsTouchToOpenSetting = prefs?.getBoolean(TOUCH_TO_OPEN_KEY, true)!!

            val themeKey = prefsContext?.getString(R.string.prefs_theme_key)
            prefs!!.registerOnSharedPreferenceChangeListener { _, key ->
                when (key) {
                    themeKey -> {
                        prefsThemeCurrentThemeSetting = prefs?.getString(themeKey, themeEntryValues[0])!!
                        setThemeOnUI()
                    }
                }
            }
        }
    }

    fun updateTheme(sp: SharedPreferences?, r: Resources?) {
        try {
            themeEntryValues = r?.getStringArray(R.array.theme_array_entry_values) as Array<String>
            prefsThemeCurrentThemeSetting = sp?.getString(THEME_KEY, themeEntryValues[0])!!
            setThemeOnUI()

        } catch (e: Exception) {
            Timber.e(e, "updateTheme exception")
        }
    }

    fun updateTouchToOpen(sp: SharedPreferences?, r: Resources?) {
        try {
            prefsTouchToOpenSetting = sp?.getBoolean(TOUCH_TO_OPEN_KEY, true)!!
        } catch (e: Exception) {
            Timber.e(e, "updateTouchToOpen exception")
        }
    }

    fun setThemeOnUI() = scope.launch {
        launch(Dispatchers.Main.immediate) {
            when (prefsThemeCurrentThemeSetting) {
                themeEntryValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeEntryValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> {
                    if (Defs.TEN_Q_GOOD_BUDDY) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
        }
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext!!)
        val set = prefs.getStringSet(key, defaultValue)
        return if (set == null) null else Collections.unmodifiableSet(set)
    }

    fun setPreviousSearchesList(prev: Set<String>) {
        prefsPreviousSearchesSet = prev.toMutableSet()
        setPreviousSearchesPref(prev)
    }

    fun setPreviousSearchesPref(value: Set<String?>) = scope.launch {
        launch(Dispatchers.IO) {
            prefs?.edit()?.putStringSet(PREVIOUS_SEARCHES_KEY, value)?.apply()
        }
    }

    // Where applicable - keys must mach strings.xml values
    const val THEME_KEY = "themekey"
    const val TOUCH_TO_OPEN_KEY = "touchtoopen"
    // independent - has no equivalent in the root_preferences.xml
    const val PREVIOUS_SEARCHES_KEY = "psk"
}