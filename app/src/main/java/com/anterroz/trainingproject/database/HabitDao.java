package com.anterroz.trainingproject.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HabitDao {


    @Query("SELECT * FROM habit ")
    List<HabitEntry> loadAllHabits();

    @Insert
    void insertHabit(HabitEntry habitEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateHabit(HabitEntry habitEntry);

    @Delete
    void deleteHabit(HabitEntry habitEntry);


}
