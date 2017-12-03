package com.betatech.alex.zodis.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.betatech.alex.zodis.R;

/**
 * Created by lenovo on 12/3/2017.
 */

public class ZodisWidgetService extends IntentService {

    public static final String ACTION_UPDATE_ALL_WIDGET = "com.betatech.alex.zodis.widgets";

    public ZodisWidgetService() {
        super(ZodisWidgetService.class.getSimpleName());
    }

    /**
     * Starts this service to update widget action. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateAllWidgets(Context context) {
        Intent intent = new Intent(context, ZodisWidgetService.class);
        intent.setAction(ACTION_UPDATE_ALL_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_ALL_WIDGET.equals(action)) {
                handleActionUpdateAllWidgets();
            }
        }
    }

    private void handleActionUpdateAllWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ZodisWidget.class));
        //Now update all widgets
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
        ZodisWidget.updateAllWidgets(this, appWidgetManager,appWidgetIds);
    }
}
