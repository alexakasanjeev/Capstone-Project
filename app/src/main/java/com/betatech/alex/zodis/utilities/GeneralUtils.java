package com.betatech.alex.zodis.utilities;

import android.database.Cursor;

import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.RootWord;

/**
 * Created by lenovo on 12/4/2017.
 */

public class GeneralUtils {

    public static RootWord extractRootWord(Cursor cursor,int position){

        if (cursor==null || position<0 && position>=cursor.getCount()) return null;

        cursor.moveToPosition(position);

        int idColumnIndex = cursor.getColumnIndex(ZodisContract.RootEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_DESCRIPTION);

        return new RootWord(cursor.getInt(idColumnIndex),cursor.getString(nameColumnIndex),cursor.getString(descriptionColumnIndex));
    }
}
