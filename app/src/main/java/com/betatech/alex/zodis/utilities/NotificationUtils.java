package com.betatech.alex.zodis.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.ui.MainActivity;

/**
 * Notification Logic
 */

public class NotificationUtils {

    public static final String ANDROID_CHANNEL_ID = "com.betatech.alex.zodis";
    public static final String ANDROID_CHANNEL_NAME = "ZODIS";
    public static final String ANDROID_CHANNEL_DESCRIPTION = "Remind user to practise";
    public static final String NOTIFICATION_ID = "notificationId";

    public static void notifyUserToPractise(Context context) {
        /*Initialize channel Id*/
        initChannels(context);

        String userName = ZodisPreferences.getUserNamePref(context).split(" ")[0];

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,ANDROID_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_dumbbell)
                        .setContentTitle(context.getString(R.string.notification_title, userName))
                        .setContentText(context.getString(R.string.notification_description))
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        int mNotificationId = 007;
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

    }

    private static void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(ANDROID_CHANNEL_DESCRIPTION);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
