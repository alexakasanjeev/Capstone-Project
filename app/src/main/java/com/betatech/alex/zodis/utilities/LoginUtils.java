package com.betatech.alex.zodis.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Helps create require helper variables that will be use to create Sign In
 */

public class LoginUtils {

    public static GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.OnConnectionFailedListener listener){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with the options specified by gso.

        return new GoogleApiClient.Builder(context)

                .enableAutoManage( (FragmentActivity)context, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public static void initUserDetails(Context context,GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();

        String personName = context.getString(R.string.default_user_name);
        Uri personPhoto = null;

        if(acct != null){
            personName = acct.getDisplayName();
            personPhoto = acct.getPhotoUrl();
        }

        ZodisPreferences.saveLoggedInPref(context,true);
        ZodisPreferences.saveUserNamePref(context,personName);
        if (personPhoto != null) {
            ZodisPreferences.savePhotoUrlPref(context,personPhoto.toString());
        }
    }
}
