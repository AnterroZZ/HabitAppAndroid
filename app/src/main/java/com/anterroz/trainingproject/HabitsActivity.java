package com.anterroz.trainingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;
import com.anterroz.trainingproject.utilities.NotificationReceiver;

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
       mRecyclerView.setItemAnimator(new DefaultItemAnimator());


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
               Intent intent = new Intent(getBaseContext(), NotificationReceiver.class);
               PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,intent,0);
               AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
               manager.cancel(pendingIntent);
           }
       }).attachToRecyclerView(mRecyclerView);
       setupViewModel();
    }

    public void addNewHabit(View view) {
        startActivity(new Intent(this,AddHabitActivity.class));
        Intent addHabitIntent = new Intent(this,AddHabitActivity.class);
    }


    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getHabits().observe(this, new Observer<List<HabitEntry>>() {
            @Override
            public void onChanged(List<HabitEntry> habitEntries) {
                Log.d(TAG,"Receiving database update (Live data) from ViewModel");
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
