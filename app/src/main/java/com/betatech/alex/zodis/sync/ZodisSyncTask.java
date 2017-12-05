package com.betatech.alex.zodis.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.utilities.NetworkUtils;
import com.betatech.alex.zodis.utilities.ZodisJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * Parse JSON data stored in assets folder and populate the database with this data.
 * It logs error message if something goes wrong.
 */

class ZodisSyncTask {
    private static final String TAG = ZodisSyncTask.class.getSimpleName();

    synchronized static boolean syncZodis(Context context) {

        boolean status = false;

        try {

            URL weatherRequestUrl = NetworkUtils.getUrl(context);


            String jsonResult = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            if (jsonResult != null && jsonResult.length() > 0) {
                ArrayList<ContentProviderOperation> contentProviderOperations = ZodisJsonUtils.getZodisContentProviderOperationsFromJson(jsonResult);

                if (contentProviderOperations.size() > 0) {

                    /*Delete old data*/
                    context.getContentResolver().delete(ZodisContract.RootEntry.CONTENT_URI,null,null);
                    context.getContentResolver().delete(ZodisContract.DerivedEntry.CONTENT_URI,null,null);
                    context.getContentResolver().delete(ZodisContract.LevelEntry.CONTENT_URI,null,null);

                    /*Populate the database with new data*/
                    context.getContentResolver().applyBatch(ZodisContract.CONTENT_AUTHORITY, contentProviderOperations);
                    /*Save the status of database into SharedPreference*/
                    ZodisPreferences.saveDatabaseStatusPref(context, true);

                    status= true;
                }
            } else {
                Log.e(TAG, "syncZodis: Unable to fetch JSON data");
            }
        } catch (IOException e) {
            Log.e(TAG, "syncZodis: Unable to open file");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "syncZodis: Unable to parse JSON");
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

        return status;
    }

}
