package com.anterroz.trainingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import java.util.Date;

public class AddHabitActivity extends AppCompatActivity {

    private RadioButton OneMinute;
    private RadioButton FifteenMinutes;
    private RadioButton ThirtyMinutes;
    private RadioButton OneHour;
    private EditText mTitle;
    private HabitsDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        mDatabase = HabitsDatabase.getInstance(getApplicationContext());
        mTitle = findViewById(R.id.new_habit_title);
        OneMinute = findViewById(R.id.new_habit_time_one_minute);
        FifteenMinutes = findViewById(R.id.new_habit_time_fifteen_minutes);
        ThirtyMinutes = findViewById(R.id.new_habit_time_thirty_minutes);
        OneHour = findViewById(R.id.new_habit_time_one_hour);

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

        if(title.equals(""))
        {
            Toast.makeText(this,getString(R.string.new_habit_no_habit_title),Toast.LENGTH_SHORT).show();
        } else {
            int time = getTimeFromButtons();
            int imageViewId = R.drawable.ic_add_circle_black_24dp;
            Date date = new Date();

            final HabitEntry habitEntry = new HabitEntry(title, imageViewId, date, time);
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.habitDao().insertHabit(habitEntry);
                }
            });
            finish();
        }

    }
}
