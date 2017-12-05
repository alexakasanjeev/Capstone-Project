package com.betatech.alex.zodis.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.betatech.alex.zodis.ui.MainActivity;

/**
 * Created by lenovo on 11/30/2017.
 */

public class ZodisSyncIntentService extends IntentService {

    public ZodisSyncIntentService() {
        super(ZodisSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ZodisSyncTask.syncZodis(this)) {
            sendMessage(1);
        }else{
            sendMessage(0);
        }


    }

    private void sendMessage(int status_code) {
        Intent intent = new Intent(MainActivity.ZODIS_SYNC_TASK_SERVICE);
        // You can also include some extra data.
        intent.putExtra(MainActivity.SERVICE_RESULT, status_code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
