package com.anterroz.trainingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anterroz.trainingproject.TabLayoutFragments.HabitFragment;
import com.anterroz.trainingproject.TabLayoutFragments.PreferenceFragment;
import com.anterroz.trainingproject.TabLayoutFragments.ProfileFragment;
import com.anterroz.trainingproject.TabLayoutFragments.SettingsFragment;
import com.google.android.material.tabs.TabLayout;

public class MainTabActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainTabAdapter mAdapter;
    private ProfileFragment mProfile;
    private SettingsFragment mSettings;
    private HabitFragment mHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        mTabLayout = findViewById(R.id.main_tab_tab_layout);
        mViewPager = findViewById(R.id.main_tab_view_pager);

        mProfile = new ProfileFragment();
        mHabits = new HabitFragment();
        mSettings = new SettingsFragment();


        mAdapter = new MainTabAdapter(getSupportFragmentManager());
        mAdapter.addFragment(mProfile,"Profile");
        mAdapter.addFragment(mHabits,"Habits");
        mAdapter.addFragment(mSettings,"Settings");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_profile_icon);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_habits_icon);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_settings_icon);

        mViewPager.setCurrentItem(1);
    }

    public void addNewHabit(View view) {
        Intent intent = new Intent(MainTabActivity.this, AddHabitActivity.class);
//        intent.putExtra(AddHabitActivity.EXTRA_TAG,itemId);
        startActivity(intent);
    }
}
