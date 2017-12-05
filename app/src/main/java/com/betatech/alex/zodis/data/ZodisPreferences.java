package com.betatech.alex.zodis.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.betatech.alex.zodis.R;

public class ZodisPreferences {

    /*
     * We will use boolean value to know database state
     *  true  : Database is populated
     *  false : Database is empty
     */
    private static final String PREF_DB_STATUS = "db";

    /*
     * In order to know whether user has logged in using google+ account
     *  true  : user has logged in
     *  false : user hasn't logged in
     */
    private static final String PREF_LOGGED_IN = "loggedIn";

    /*
     * If user has logged in we will cache his/her profile name.
     */
    private static final String PREF_USER_NAME = "user_name";

    /*
     * If user has logged in we will cache the url pointing to user profile picture.
     */
    private static final String PREF_USER_PHOTO_URL = "user_photo_url";

    /*
     * Stores the experience score(XP) user get when he/she completes a quiz.
     * Completing one quiz increase the XP by 10.
     */
    private static final String PREF_XP_EARN = "xp";

    /*
     * We will show LoginActivity only once in entire life span of our app.
     * User can log in from inside the app also.
     */
    private static final String PREF_FIRST_TIME = "first_time_app_open";


    /**
     * Returns whether app has been launched for the first time. The default value this method
     * will return is false which means this is the first time user is launching the app.
     *
     * @param context Context used to access ZodisPreferences
     * @return true if app has been initiated at least one time, false if not
     */
    public static Boolean getFirstTimePref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getBoolean(PREF_FIRST_TIME,false);
    }

    /**
     * Helper method to save app has been launched at least once in Preferences. To avoid showing
     * LoginActivity to user second time he/she launch app.
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     * @param status   if true means app has been initiated at least one time
     */
    public static void saveFirstTimePref(Context context, boolean status) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(PREF_FIRST_TIME, status);
        editor.apply();
    }

    /**
     * Returns true if data has been successfully fetched and parsed over the network else
     * returns false which means database is empty
     *
     * @param context Context used to access ZodisPreferences
     * @return true if database is filled, false if not
     */
    public static Boolean getDatabaseStatusPref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getBoolean(PREF_DB_STATUS,false);
    }

    /**
     * Helper method to save the current database state. If saved true it means
     * app was successful in retrieving data and parsing it.
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     * @param status   if true means app has been initiated at least one time
     */
    public static void saveDatabaseStatusPref(Context context, boolean status) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(PREF_DB_STATUS, status);
        editor.apply();
    }

    /**
     * Returns true if user has logged in using their google+ account. If user has logged in
     * it means name and url pointing to profile picture has been cached
     *
     * @param context Context used to access ZodisPreferences
     * @return true if user is logged in, false if not
     */
    public static Boolean getLoggedInPref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getBoolean(PREF_LOGGED_IN,false);
    }

    /**
     * Helper method to save whether user has logged in or not using their google+ account
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     * @param status   if true means user has used google+ account to log in
     */
    public static void saveLoggedInPref(Context context, boolean status) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(PREF_LOGGED_IN, status);
        editor.apply();
    }

    /**
     * Returns the XP user has earned. For each quiz user can gain 10XP
     *
     * @param context Context used to access ZodisPreferences
     * @return xp user has earned while doing Quizzes
     */
    public static int getXpEarnPref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getInt(PREF_XP_EARN,0);
    }

    /**
     * Helper method to increment the XP score by a factor of 10. Whenever user completes
     * a quiz this function will be called.
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     */
    public static void incrementXpPref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        int xp = sp.getInt(PREF_XP_EARN,0);
        editor.putInt(PREF_XP_EARN, xp+10);
        editor.apply();
    }

    /**
     * Returns the user name. Calling this function means user has logged in at some point.
     *
     * @param context Context used to access ZodisPreferences
     * @return name of user saved in it's google profile
     */
    public static String getUserNamePref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_USER_NAME,context.getString(R.string.default_user_name));
    }

    /**
     * Helper method to save user name. Calling this function means user has logged in.
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     * @param name represents the name from user google+ account
     */
    public static void saveUserNamePref(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_USER_NAME, name);
        editor.apply();
    }

    /**
     * Returns the url pointing towards user profile picture on his/her google+ account
     *
     * @param context Context used to access ZodisPreferences
     * @return url for the user picture
     */
    public static String getPhotoUrlPref(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_USER_PHOTO_URL,"");
    }

    /**
     * Helper method to save url that will be used to show user profile pic in UserFragment
     *
     * <p>
     *
     * @param context  Context used to get the ZodisPreferences
     * @param url for user profile picture
     */
    public static void savePhotoUrlPref(Context context, String url) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_USER_PHOTO_URL, url);
        editor.apply();
    }
}
