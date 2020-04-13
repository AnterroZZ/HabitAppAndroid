package com.anterroz.trainingproject.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.anterroz.trainingproject.AddHabitActivity;
import com.anterroz.trainingproject.HabitsActivity;
import com.anterroz.trainingproject.R;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtils {

    private static final int PENDING_INTENT_ID = 1111;
    private static final int NOTIFICATION_ID = 1231;
    private static final String NOTIFICATION_CHANNEL_ID = "channelid";

    public static void remindUser(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorBackground))
                .setLargeIcon(largeIcon(context))
                .setSmallIcon(R.drawable.ic_gym)
                .setContentTitle("You've been notified!")
                .setContentText("Its time to hit the gym!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Its time to hit the gym!"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());


    }
    private static PendingIntent contentIntent(Context context)
    {
        Intent startActivityIntent = new Intent(context, AddHabitActivity.class);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }

    private static Bitmap largeIcon(Context context)
    {
        Resources resources = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_gym);
        return largeIcon;

    }


}
