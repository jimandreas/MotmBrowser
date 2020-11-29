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

@file:Suppress("UnnecessaryVariable", "MoveVariableDeclarationIntoWhen", "MemberVisibilityCanBePrivate")

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

    private var prefsThemeCurrentThemeSetting: String = ""

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
            prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)

            updateTheme(prefs, prefsContext!!.resources)

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
            val themeKey = r?.getString(R.string.prefs_theme_key)
            themeEntryValues = r?.getStringArray(R.array.theme_array_entry_values) as Array<String>
            prefsThemeCurrentThemeSetting = sp?.getString(themeKey, themeEntryValues[0])!!
            setThemeOnUI()

        } catch (e: Exception) {
            Timber.e(e, "updateTheme exception")
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
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)
        val set = prefs.getStringSet(key, defaultValue)
        return if (set == null) null else Collections.unmodifiableSet(set)
    }

    fun setStringSet(key: String?, value: Set<String?>?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)
        prefs.edit().putStringSet(key, value).apply()
    }

    fun touchToOpenPdb(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(prefsContext)
        val key = prefsContext?.resources?.getString(R.string.settings_one_touch_open_key)
        val currentSetting = prefs.getBoolean(key, true)
        return currentSetting
    }

    const val PREVIOUS_SEARCHES_KEY = "psk"
}