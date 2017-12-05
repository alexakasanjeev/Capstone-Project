package com.betatech.alex.zodis.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Simple logic to check network connection
 */

public class NetworkUtils {

    private static final String STATIC_ZODIS_URL =
            "https://raw.githubusercontent.com/alexakasanjeev/zodisDatabase/master/document.json";


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

    public static URL getUrl(Context context) throws MalformedURLException {

        return new URL(STATIC_ZODIS_URL);
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}