package com.betatech.alex.zodis.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by lenovo on 11/30/2017.
 */

public class ZodisSyncIntentService extends IntentService {

    public ZodisSyncIntentService() {
        super(ZodisSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ZodisSyncTask.syncZodis(this);
    }
}
