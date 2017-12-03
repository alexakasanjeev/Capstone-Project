package com.betatech.alex.zodis.ui.tabs.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.utilities.ImageUtils;
import com.betatech.alex.zodis.utilities.LoginUtils;
import com.betatech.alex.zodis.utilities.NetworkUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.button_sign_in) Button signInButton;
    @BindView(R.id.linear_guest_profile) LinearLayout guestProfileLinearLayout;
    @BindView(R.id.linear_user_profile) LinearLayout userProfileLinearLayout;
    @BindView(R.id.image_user_profile_pic) ImageView userProfileImageView;
    @BindView(R.id.text_user_name) TextView userNameTextView;
    @BindView(R.id.text_user_xp_score) TextView xpEarnedTextView;
    @BindView(R.id.text_user_lesson_completed) TextView lessonCompletedTextView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private GoogleApiClient mGoogleApiClient = null;
    private static final int RC_SIGN_IN = 007;
    private static final String TAG = "zodis";

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,view);

        if (ZodisPreferences.getLoggedInPref(getActivity())) {
            showUserProfile();
        }else{
            showGuestProfile();
            /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleApiClient with the options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            FragmentActivity fragmentActivity;*/
            mGoogleApiClient = LoginUtils.getGoogleApiClient(getActivity(),this);
        }


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        // stop GoogleApiClient
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @OnClick(R.id.button_sign_in)
    public void signIn(){
        if (NetworkUtils.isOnline(getActivity())) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
            showProgressBar();
        }else{
            Toast.makeText(getActivity(), R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.d(TAG, "handleSignInResult: result successful");
            LoginUtils.initUserDetails(getActivity(),result);
            showUserProfile();
        }else{
            Log.d(TAG, "handleSignInResult: result failed");
        }
    }



    private void showGuestProfile(){
        hideProgressBar();
        guestProfileLinearLayout.setVisibility(View.VISIBLE);
        userProfileLinearLayout.setVisibility(View.GONE);
    }

    private void showUserProfile(){
        hideProgressBar();
        guestProfileLinearLayout.setVisibility(View.GONE);
        userProfileLinearLayout.setVisibility(View.VISIBLE);

        String photoUrlPref = ZodisPreferences.getPhotoUrlPref(getActivity());
        String userName = ZodisPreferences.getUserNamePref(getActivity());
        int userXpScore = ZodisPreferences.getXpEarnPref(getActivity());
        int userLessonCompleted = ZodisPreferences.getLessonCompletedPref(getActivity());

        userNameTextView.setText(userName);
        xpEarnedTextView.setText(getString(R.string.xp_message,userXpScore));
        lessonCompletedTextView.setText(getString(R.string.level_messages,userLessonCompleted,10));

        if (photoUrlPref!=null && photoUrlPref.length()>0) {
            ImageUtils.loadImage(getActivity(),userProfileImageView, photoUrlPref);
        }else{
            userProfileImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
        }

    }

    private void showProgressBar(){
        guestProfileLinearLayout.setVisibility(View.GONE);
        userProfileLinearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG","Connection Failed");
        Toast.makeText(getActivity(), "Unable to connect", Toast.LENGTH_SHORT).show();
    }
}
