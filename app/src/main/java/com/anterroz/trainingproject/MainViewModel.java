package com.anterroz.trainingproject;

import android.app.Application;
import android.util.Log;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<HabitEntry>> habits;
    private static final String TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG,"Retrieving habits from DataBase..");
        HabitsDatabase database = HabitsDatabase.getInstance(this.getApplication());
        habits = database.habitDao().loadAllHabits();
    }

    public LiveData<List<HabitEntry>> getHabits() {
        return habits;
    }
}
