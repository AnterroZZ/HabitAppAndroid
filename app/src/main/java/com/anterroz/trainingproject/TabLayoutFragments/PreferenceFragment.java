package com.anterroz.trainingproject.TabLayoutFragments;

import android.os.Bundle;

import com.anterroz.trainingproject.R;

import androidx.preference.PreferenceFragmentCompat;

public class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref,rootKey);
    }
}
