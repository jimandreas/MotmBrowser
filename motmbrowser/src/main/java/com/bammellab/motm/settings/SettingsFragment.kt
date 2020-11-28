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

@file:Suppress("UNUSED_VARIABLE", "MoveVariableDeclarationIntoWhen")

package com.bammellab.motm.settings

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bammellab.motm.BuildConfig
import com.bammellab.motm.R
import com.bammellab.motm.util.Defs.TEN_Q_GOOD_BUDDY
import com.bammellab.motm.util.PrefsUtil
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    private var themePreference: ListPreference? = null
    private var savedResources: Resources? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        savedResources = resources
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val context = preferenceScreen.context

        themePreferenceHandler()

        val versionPreference : Preference? = findPreference("app_version")
        val currentVersionString = BuildConfig.VERSION_NAME
        versionPreference!!.summary = currentVersionString
    }

    /**
     * Adjust the Theme list preference
     * With TenQAPI29 and above the Android platform has a system Dark Mode setting.
     * Earlier it was something like a strange Battery Saver.
     * This function hacks the pref list appropriately.
     */
    private fun themePreferenceHandler() {

        try {
            val entries = resources.getStringArray(R.array.theme_array_entries).toMutableList()
            val entryValues = resources.getStringArray(R.array.theme_array_entry_values).toMutableList()

            themePreference = findPreference(resources.getString(R.string.prefs_theme_key))
            if (TEN_Q_GOOD_BUDDY) {
                entries.remove(entries[2])
                entryValues.remove(entryValues[2])
            } else {
                entries.remove(entries[3])
                entryValues.remove(entryValues[3])
            }
            themePreference?.entries = entries.toTypedArray()
            themePreference?.entryValues = entryValues.toTypedArray()
        } catch (e: Exception) {
            Timber.e(e, "Something bad happened in themePreferenceHandler")
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {

        val themeKey = savedResources!!.getString(R.string.prefs_theme_key)
        if (key == themeKey) {
            PrefsUtil.updateTheme(sp, savedResources)
        }
    }

    companion object {

    }
}


