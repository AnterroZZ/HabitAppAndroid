package com.anterroz.trainingproject.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;

public class NotificationWorker extends Worker {

    Context mContext;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        sendNotification();
        return Result.success();
    }

    public void sendNotification()
    {
        NotificationUtils.remindUser(mContext);
    }
}
