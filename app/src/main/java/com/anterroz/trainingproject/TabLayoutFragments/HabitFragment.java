package com.anterroz.trainingproject.TabLayoutFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anterroz.trainingproject.AddHabitActivity;
import com.anterroz.trainingproject.AppExecutor;
import com.anterroz.trainingproject.HabitsAdapter;
import com.anterroz.trainingproject.MainViewModel;
import com.anterroz.trainingproject.R;
import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HabitFragment extends Fragment implements HabitsAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private HabitsAdapter mAdapter;
    private HabitsDatabase mDatabase;
    private Context mContext;
    private static final String TAG = HabitFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit, container, false);
        mContext = getActivity();
        mDatabase = HabitsDatabase.getInstance(getActivity());


        /* Setting up RecyclerView and
        attaching HabitsAdapter to it
         */
        mRecyclerView = rootView.findViewById(R.id.fragment_habits_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new HabitsAdapter(getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<HabitEntry> habitEntries = mAdapter.getHabits();
                        mDatabase.habitDao().deleteHabit(habitEntries.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
        setupViewModel();
        return rootView;
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(getActivity(), AddHabitActivity.class);
        intent.putExtra(AddHabitActivity.EXTRA_TAG,itemId);
        startActivity(intent);
    }

    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getHabits().observe(this, new Observer<List<HabitEntry>>() {
            @Override
            public void onChanged(List<HabitEntry> habitEntries) {
                Log.d(TAG,"Receiving database update (Live data) from ViewModel");
                Collections.sort(habitEntries, new Comparator<HabitEntry>() {
                    @Override
                    public int compare(HabitEntry o1, HabitEntry o2) {
                        return String.valueOf(o2.getTimeInSeconds()).compareTo(String.valueOf(o1.getTimeInSeconds()));
                    }
                });
                mAdapter.setHabits(habitEntries);
            }
        });
    }
}