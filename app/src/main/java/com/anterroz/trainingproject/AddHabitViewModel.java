package com.anterroz.trainingproject;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddHabitViewModel extends ViewModel {

    private LiveData<HabitEntry> habit;

    public AddHabitViewModel(HabitsDatabase database, int habitId) {
        this.habit = database.habitDao().loadHabitById(habitId);
    }

    public LiveData<HabitEntry> getHabit() {
        return habit;
    }
}
