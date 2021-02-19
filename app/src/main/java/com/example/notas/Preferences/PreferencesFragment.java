package com.example.notas.Preferences;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.notas.R;


public class PreferencesFragment extends PreferenceFragmentCompat {

    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_fragment);
    }
}
