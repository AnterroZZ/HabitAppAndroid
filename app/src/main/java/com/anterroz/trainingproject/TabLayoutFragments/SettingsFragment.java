package com.anterroz.trainingproject.TabLayoutFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anterroz.trainingproject.R;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private FrameLayout mContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getFragmentManager().beginTransaction().replace(R.id.settings_preference_place_holder,new PreferenceFragment()).commit();
        return view;
    }
}