package com.betatech.alex.zodis.ui.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.ui.lesson.LessonActivity;
import com.betatech.alex.zodis.utilities.QuizUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.plus.PlusShare;

import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.google.android.gms.plus.PlusShare;

public class ShareActivity extends AppCompatActivity {

    /**
     * Showing Interstitial Ad when user click Continue button
     */
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setupWindowAnimations();

        ButterKnife.bind(this);

        int lessonId;

        if (getIntent() != null) {
            lessonId = getIntent().getIntExtra(LessonActivity.KEY_LESSON_ID, -1);
            QuizUtils.increaseXP(this, String.valueOf(lessonId));
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitialId));

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(request);
    }

    private void setupWindowAnimations() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }
    }

    @OnClick(R.id.button_share_score)
    public void shareScore() {
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(getString(R.string.google_plus_share_message))
                /*This URL will be replaced with Google play url to download app,
                 * but currently I don't have google play account
                 * so using dummy URL*/
                .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                .getIntent();

        startActivityForResult(shareIntent, 0);
        finish();
    }

    @OnClick(R.id.button_continue)
    public void exit() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        finish();
    }
}
