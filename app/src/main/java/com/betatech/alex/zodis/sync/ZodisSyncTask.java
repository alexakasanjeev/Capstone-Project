package com.betatech.alex.zodis.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.utilities.ZodisJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by lenovo on 11/30/2017.
 */

public class ZodisSyncTask {
    private static final String TAG  = "ZODIS";

    synchronized public static void syncZodis(Context context){

        try {
            String jsonResult = loadJSONFromAsset(context);
            if (jsonResult!=null && jsonResult.length()>0) {
                ArrayList<ContentProviderOperation> contentProviderOperations = ZodisJsonUtils.getZodisContentProviderOperationsFromJson(jsonResult);

                if (contentProviderOperations.size()>0) {
                    context.getContentResolver().applyBatch(ZodisContract.CONTENT_AUTHORITY,contentProviderOperations);
                    /*Save the status of database into SharedPreference*/
                    ZodisPreferences.saveDatabaseStatusPref(context,true);
                }
            }else{
                Log.e(TAG, "syncZodis: Unable to read file");
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
    }



    private static String loadJSONFromAsset(Context context) throws IOException {
        String json = null;
        try {
            InputStream is = context.getAssets().open("document.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
