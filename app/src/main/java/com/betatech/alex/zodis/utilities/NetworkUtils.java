package com.betatech.alex.zodis.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Simple logic to check network connection
 */

public class NetworkUtils {


    private NetworkUtils() {
    }

    public static final String TAG = NetworkUtils.class.getSimpleName();

    /*
    * isOnline - Check if there is a NetworkConnection
    * @return boolean
    */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}