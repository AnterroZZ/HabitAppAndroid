package com.anterroz.trainingproject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    //for singleton pattern
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIO;
    //TODO: Implement network database
    private final Executor networkIO;

    private AppExecutor(Executor diskIO, Executor networkIO)
    {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public static AppExecutor getInstance()
    {
        if(sInstance==null)
        {
            sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3));
        }
        return sInstance;
    }

    public Executor diskIO()
    {
        return diskIO;
    }

    public Executor networkIO()
    {
        return networkIO;
    }
}
