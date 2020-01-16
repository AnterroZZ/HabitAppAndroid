package com.anterroz.trainingproject;

import com.anterroz.trainingproject.database.HabitsDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddHabitViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final HabitsDatabase mDatabase;
    private final int mHabitId;

    public AddHabitViewModelFactory(HabitsDatabase habitsDatabase, int mHabitId)
    {
        this.mDatabase = habitsDatabase;
        this.mHabitId = mHabitId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddHabitViewModel(mDatabase,mHabitId);
    }
}
