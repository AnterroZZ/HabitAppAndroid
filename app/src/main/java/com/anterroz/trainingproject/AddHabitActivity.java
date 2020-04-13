package com.anterroz.trainingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;
import com.anterroz.trainingproject.utilities.NotificationReceiver;
import com.anterroz.trainingproject.utilities.NotificationWorker;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AddHabitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RadioButton OneMinute;
    private RadioButton FifteenMinutes;
    private RadioButton ThirtyMinutes;
    private RadioButton OneHour;
    private EditText mTitle;
    private HabitsDatabase mDatabase;
    private Spinner mCategorySpinner;
    private int imageView;
    private ImageView mImageView;
    private Button mButton;
    private static final int DEFAULT_TASK_ID = -1;


    private int mHabitId = DEFAULT_TASK_ID;

    public static final String TAG = AddHabitActivity.class.getSimpleName();
    public static final String EXTRA_TAG = "updateItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        mButton = findViewById(R.id.new_habit_done_button);
        mDatabase = HabitsDatabase.getInstance(getApplicationContext());
        mTitle = findViewById(R.id.new_habit_title);
        OneMinute = findViewById(R.id.new_habit_time_one_minute);
        FifteenMinutes = findViewById(R.id.new_habit_time_fifteen_minutes);
        ThirtyMinutes = findViewById(R.id.new_habit_time_thirty_minutes);
        OneHour = findViewById(R.id.new_habit_time_one_hour);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_TAG))
        {
            mButton.setText(getString(R.string.new_habit_button_update));
            if(mHabitId == DEFAULT_TASK_ID)
            {
                mHabitId = intent.getIntExtra(EXTRA_TAG,DEFAULT_TASK_ID);
                AddHabitViewModelFactory factory = new AddHabitViewModelFactory(mDatabase,mHabitId);
                final AddHabitViewModel viewModel = ViewModelProviders.of(this,factory).get(AddHabitViewModel.class);
                viewModel.getHabit().observe(this, new Observer<HabitEntry>() {
                    @Override
                    public void onChanged(HabitEntry habit) {
                        viewModel.getHabit().removeObserver(this);
                        Log.d(TAG, "Receiving database update from LiveData");
                        populateUI(habit);
                    }
                });
            }
        }




        //Setting up the spinner and it's adapter
        mCategorySpinner = findViewById(R.id.new_habit_category_spinner);
        mImageView = findViewById(R.id.new_habit_icon);

        mCategorySpinner.setOnItemSelectedListener(this);


        //Setting up every button for the time
        OneMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonsCheck(true,false,false,false);
            }
        });
        FifteenMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonsCheck(false,true,false,false);
            }
        });
        ThirtyMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonsCheck(false,false,true,false);
            }
        });
        OneHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonsCheck(false,false,false,true);
            }
        });
    }


    private void radioButtonsCheck(boolean oneMinute, boolean fifteenMinutes,
                                   boolean thirtyMinutes, boolean oneHour)
    {
        OneMinute.setChecked(oneMinute);
        FifteenMinutes.setChecked(fifteenMinutes);
        ThirtyMinutes.setChecked(thirtyMinutes);
        OneHour.setChecked(oneHour);
    }

    public boolean areAnyRadioButtonChecked()
    {
        return (OneMinute.isChecked() || FifteenMinutes.isChecked() || ThirtyMinutes.isChecked() || OneHour.isChecked());
    }

    private int getTimeFromButtons()
    {
        if(OneMinute.isChecked())
        {
            return getTimeFinished(1);
        }else if(FifteenMinutes.isChecked())
        {
            return getTimeFinished(15);
        }else if(ThirtyMinutes.isChecked())
        {
            return getTimeFinished(30);
        }else
        {
            return getTimeFinished(60);
        }
    }


    public void AddNewHabit(View view) {
        String title = mTitle.getText().toString();
        String tag = title + Math.random();
        String category = mCategorySpinner.getSelectedItem().toString();

        if(title.equals(""))
        {
            Toast.makeText(this,getString(R.string.new_habit_no_habit_title),Toast.LENGTH_LONG).show();
        } else if(!areAnyRadioButtonChecked())
        {
            Toast.makeText(this,getString(R.string.new_habit_no_time),Toast.LENGTH_LONG).show();
        } else {
            int time = getTimeFromButtons();
            PeriodicWorkRequest notificationRequest2 = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15*60, TimeUnit.SECONDS)
                    .setInitialDelay(time,TimeUnit.SECONDS)
                    .addTag(tag)
                    .build();
            WorkManager workManager = WorkManager.getInstance();
            workManager.enqueue(notificationRequest2);

            Date date = new Date();
            Intent intent = new Intent(getBaseContext(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),1,intent,0);

            long tenSeconds = 1000*10;
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime()+ tenSeconds, tenSeconds, pendingIntent);

            //TODO: Change the attributes of habit
            final HabitEntry habitEntry = new HabitEntry(title, imageView, date, time, category,true,tag);
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(mHabitId == DEFAULT_TASK_ID)
                    {
                        mDatabase.habitDao().insertHabit(habitEntry);
                    }else {
                        habitEntry.setId(mHabitId);
                        mDatabase.habitDao().updateHabit(habitEntry);
                    }
                }
            });
            finish();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                imageView = R.drawable.ic_gym;
                mImageView.setImageResource(imageView);
                break;
            case 1:
                imageView = R.drawable.ic_self_development;
                mImageView.setImageResource(imageView);
                break;
            case 2:
                imageView = R.drawable.ic_health;
                mImageView.setImageResource(imageView);
                break;
            case 3:
                imageView = R.drawable.ic_other;
                mImageView.setImageResource(imageView);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void populateUI(HabitEntry habit) {
        if (habit == null) {
            return;
        }

        mTitle.setText(habit.getTitle());
        String habitCategory = habit.getCategory();
        switch (habitCategory)
        {
            case "Exercises":
                mCategorySpinner.setSelection(0);
                break;
            case "Self Development":
                mCategorySpinner.setSelection(1);
                break;
            case "Health":
                mCategorySpinner.setSelection(2);
                break;
            case "Other":
                mCategorySpinner.setSelection(3);
                break;

        }
        int time = habit.getTimeInSeconds();
        switch (time)
        {
            case 60:
                OneMinute.setChecked(true);
                break;
            case 15*60:
                FifteenMinutes.setChecked(true);
                break;
            case 30*60:
                ThirtyMinutes.setChecked(true);
                break;
            case 60*60:
                OneHour.setChecked(true);
                break;
        }
    }

    private int getTimeFinished(int minutes)
    {
        //TODO: Change to 60000 after beeing done with reseting timer
        return ( (int) new Date().getTime() + 60000*minutes);
    }
}
