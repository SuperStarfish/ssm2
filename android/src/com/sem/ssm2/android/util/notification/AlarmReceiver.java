package com.sem.ssm2.android.util.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import com.sem.ssm2.android.AndroidLauncher;
import com.sem.ssm2.android.R;


/**
 * A receiver for the alarm to display the notification.
 */
public class AlarmReceiver extends BroadcastReceiver {
    /**
     * Times for the vibration and led blinking.
     */
    protected final int cOnTime = 500, cOffTime = 200;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Intent resultIntent = new Intent(context, AndroidLauncher.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Super StarFish Mania")
                        .setContentText("A new stroll is available!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setLights(Color.BLUE, cOnTime, cOffTime)
                        .setVibrate(new long[]{cOffTime, cOnTime});


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        notificationManager.notify(1, notificationBuilder.build());
    }
}