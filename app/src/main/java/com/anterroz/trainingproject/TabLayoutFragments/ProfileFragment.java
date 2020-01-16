package com.anterroz.trainingproject.TabLayoutFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.anterroz.trainingproject.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        mToolbar = view.findViewById(R.id.profile_toolbar);
        mToolbar.inflateMenu(R.menu.profile_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemid = item.getItemId();
                switch (itemid)
                {
                    case R.id.action_share:
                        Toast.makeText(getActivity(),"This will be used to share a profile",Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}