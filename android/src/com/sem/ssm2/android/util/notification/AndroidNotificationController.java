package com.sem.ssm2.android.util.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.badlogic.gdx.Gdx;
import com.sem.ssm2.util.NotificationController;

/**
 * Android implementation of the notification controller.
 */
public class AndroidNotificationController implements NotificationController {

    /**
     * Context to access android specific functions.
     */
    protected Context cContext;

    /**
     * Android variant of the notification controller.
     *
     * @param context Context to access android specific functions.
     */
    public AndroidNotificationController(final Context context) {
        cContext = context;
    }

    @Override
    public void scheduleNotification(final Long time) {
        NotificationManager notificationManager =
                (NotificationManager) cContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        AlarmManager alarmManager = (AlarmManager) cContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(cContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, time, pendingIntent);

        Gdx.app.log(getClass().getSimpleName(), "Scheduled notification at: " + time);
    }
}