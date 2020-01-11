package com.anterroz.trainingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import java.util.List;

public class HabitsActivity extends AppCompatActivity implements HabitsAdapter.ItemClickListener {

    private static final String TAG = HabitsActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private HabitsAdapter mAdapter;
    private HabitsDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        mDatabase = HabitsDatabase.getInstance(getApplicationContext());

        /* Setting up RecyclerView and
        attaching HabitsAdapter to it
         */
       mRecyclerView = findViewById(R.id.habits_recycler_view);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       mAdapter = new HabitsAdapter(this,this);
       mRecyclerView.setAdapter(mAdapter);


       /* Adding new ItemTouchHelper for deleting habits
       * when a user swipes and attaching it to RecyclerView*/

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
       retrieveHabits();
    }

    public void addNewHabit(View view) {
        startActivity(new Intent(this,AddHabitActivity.class));
    }


    private void retrieveHabits() {
        Log.d(TAG,"Retrieving habits from DataBase..");
        final LiveData<List<HabitEntry>> habits = mDatabase.habitDao().loadAllHabits();
        habits.observe(this, new Observer<List<HabitEntry>>() {
            @Override
            public void onChanged(List<HabitEntry> habitEntries) {
                Log.d(TAG,"Receiving database update (Live data)");
                mAdapter.setHabits(habitEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(HabitsActivity.this, AddHabitActivity.class);
        intent.putExtra(AddHabitActivity.EXTRA_TAG,itemId);
        startActivity(intent);
    }
}
