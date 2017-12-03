package com.betatech.alex.zodis.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.betatech.alex.zodis.utilities.NotificationUtils;

/**
 * Created by lenovo on 12/2/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.notifyUserToPractise(context);
    }
}
