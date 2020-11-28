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
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

object PrefsUtil {
    var prefsContext: Context? = null
    private val scope = MainScope()

    fun init() = scope.launch {
        initPreferences()
    }

    private suspend fun initPreferences() {
        if (prefsContext != null) {
            withContext(Dispatchers.IO) {
                // Listen for preference changes
                val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(prefsContext)
                updateTheme(prefs, prefsContext!!.resources)
            }
        }
    }

    private var setTo: String = ""
    private var entryValues : Array<String> = arrayOf("")

    // https://medium.com/androiddevelopers/coroutines-on-android-part-i-getting-the-background-3e0e54d20bb
    fun updateTheme(sp: SharedPreferences?, r: Resources?) = runBlocking {
        try {
            whatIsIt(sp, r) // query on background IO thread
            when (setTo) {

                entryValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                entryValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> {
                    if (Defs.TEN_Q_GOOD_BUDDY) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "updateTheme exception")
        }
    }

    /**
     * query the preferences for the theme setting
     *    Do this on an IO thread to avoid StrictMode flagging
     */
    suspend fun whatIsIt(sp: SharedPreferences?, r: Resources?) =
            withContext(Dispatchers.IO) {
                val themeKey = r?.getString(R.string.prefs_theme_key)
                entryValues = r?.getStringArray(R.array.theme_array_entry_values) as Array<String>

                setTo = sp?.getString(themeKey, entryValues[0])!!
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