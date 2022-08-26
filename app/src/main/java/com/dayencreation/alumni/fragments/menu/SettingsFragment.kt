package com.dayencreation.alumni.fragments.menu

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.dayencreation.alumni.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}