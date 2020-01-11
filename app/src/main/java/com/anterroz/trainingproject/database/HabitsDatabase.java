package com.anterroz.trainingproject.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {HabitEntry.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class HabitsDatabase extends RoomDatabase {

    private static final String LOG_TAG = HabitsDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "habitss";
    private static HabitsDatabase sInstance;

    public static HabitsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        HabitsDatabase.class, HabitsDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract HabitDao habitDao();
}
