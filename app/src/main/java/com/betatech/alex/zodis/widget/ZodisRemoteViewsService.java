package com.betatech.alex.zodis.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;

/**
 * Created by lenovo on 11/30/2017.
 */

public class ZodisRemoteViewsService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ZodisRemoteViewsFactory(getApplicationContext());
    }
}

class ZodisRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public ZodisRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }
    
    @Override
    public void onDataSetChanged() {
        // TODO: 11/30/2017 Update the where clause, show only practiced root word
        String[] projections = new String[]{
                ZodisContract.RootEntry._ID,
                ZodisContract.RootEntry.COLUMN_NAME,
                ZodisContract.RootEntry.COLUMN_DESCRIPTION};
        String selection = null;
        String[] selectionArgs = null;

        mCursor = mContext.getContentResolver().query(ZodisContract.RootEntry.CONTENT_URI,projections,selection,selectionArgs,null);
    }
    

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null) {
            return null;
        }
        
        mCursor.moveToPosition(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_zodis_widget);
        
        int wordNameColumnIndex = mCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_NAME);
        int wordDescriptionColumnIndex = mCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_DESCRIPTION);
        
        String name = mCursor.getString(wordNameColumnIndex);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String description = mCursor.getString(wordDescriptionColumnIndex);

        remoteViews.setTextViewText(R.id.text_widget_name,name);
        remoteViews.setTextViewText(R.id.text_widget_description,description);

        remoteViews.setOnClickFillInIntent(R.id.item_widget_list_container,new Intent());

        return remoteViews;
    }

    @Override
    public void onCreate() {
        //...
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if (mCursor==null) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(mCursor.getColumnIndex(ZodisContract.RootEntry._ID)): position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
   
}
