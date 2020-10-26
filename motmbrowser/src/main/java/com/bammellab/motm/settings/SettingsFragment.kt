package com.bammellab.motm.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bammellab.motm.BuildConfig
import com.bammellab.motm.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val versionPreference = findPreference("app_version")
        val currentVersionString = BuildConfig.VERSION_NAME
        versionPreference.summary = currentVersionString
    }
}


