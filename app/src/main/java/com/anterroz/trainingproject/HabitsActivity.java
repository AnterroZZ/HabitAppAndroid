package com.anterroz.trainingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import java.util.List;

public class HabitsActivity extends AppCompatActivity {

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
       mRecyclerView = findViewById(R.id.my_recycler_view);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       mAdapter = new HabitsAdapter(this);
       mRecyclerView.setAdapter(mAdapter);


       /* Adding new ItemTouchHelper for deleting habits
       * when a user swipes and attaching it to RecyclerView*/

       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
           @Override
           public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
               return false;
           }

           @Override
           public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               //TODO: (2) Delete instance of habit from the Room's database

           }
       }).attachToRecyclerView(mRecyclerView);

    }

    public void addNewHabit(View view) {
        startActivity(new Intent(this,AddHabitActivity.class));
    }

    @Override
    protected void onResume() {
        mAdapter.setHabits(mDatabase.habitDao().loadAllHabits());
        super.onResume();

    }
}
