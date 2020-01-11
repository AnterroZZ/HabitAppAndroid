package com.anterroz.trainingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Date;

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
                final LiveData<HabitEntry> habitEntry = mDatabase.habitDao().loadHabitById(mHabitId);
                habitEntry.observe(this, new Observer<HabitEntry>() {
                    @Override
                    public void onChanged(HabitEntry habit) {
                        habitEntry.removeObserver(this);
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
        if(OneMinute.isChecked() || FifteenMinutes.isChecked() || ThirtyMinutes.isChecked() || OneHour.isChecked())
        {
            return true;
        } else return false;
    }

    private int getTimeFromButtons()
    {
        if(OneMinute.isChecked())
        {
            return 1;
        }else if(FifteenMinutes.isChecked())
        {
            return 15;
        }else if(ThirtyMinutes.isChecked())
        {
            return 30;
        }else
        {
            return 60;
        }
    }


    public void AddNewHabit(View view) {
        String title = mTitle.getText().toString();
        String category = mCategorySpinner.getSelectedItem().toString();

        if(title.equals(""))
        {
            Toast.makeText(this,getString(R.string.new_habit_no_habit_title),Toast.LENGTH_LONG).show();
        } else if(!areAnyRadioButtonChecked())
        {
            Toast.makeText(this,getString(R.string.new_habit_no_time),Toast.LENGTH_LONG).show();
        } else {
            int time = getTimeFromButtons();
            Date date = new Date();

            final HabitEntry habitEntry = new HabitEntry(title, imageView, date, time, category);
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
        mImageView.setImageResource(habit.getImageViewId());
        int time = habit.getTimeInSeconds();
        switch (time)
        {
            case 1:
                OneMinute.setChecked(true);
                break;
            case 15:
                FifteenMinutes.setChecked(true);
                break;
            case 30:
                ThirtyMinutes.setChecked(true);
                break;
            case 60:
                OneHour.setChecked(true);
                break;
        }
    }
}
