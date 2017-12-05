package com.betatech.alex.zodis.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.sync.ZodisSyncIntentService;
import com.betatech.alex.zodis.ui.login.LoginActivity;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsFragment;
import com.betatech.alex.zodis.ui.tabs.levels.LevelsFragment;
import com.betatech.alex.zodis.ui.tabs.user.UserFragment;
import com.betatech.alex.zodis.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_RETAINED_USER_FRAGMENT = "user_fragment";
    private static final String TAG_RETAINED_INSIGHTS_FRAGMENT = "insights_fragment";
    private static final String TAG_RETAINED_LEVELS_FRAGMENT = "levels_fragment";
    private static final String STATE_FRAGMENT_OPENED = "fragment_number";
    public static final String ZODIS_SYNC_TASK_SERVICE = "ZodisSyncTaskService";
    public static final String SERVICE_RESULT = "ZodisSyncTaskServiceResult";
    private static final String STATE_LOADING_STARTED = "loadingStarted";
    @BindView(R.id.am_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.am_bottomNavigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.progress_bar)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.text_loading_message)
    TextView mStatusTextView;
    @BindView(R.id.linear_loading_container)
    LinearLayout loadingContainerLinearLayout;
    @BindView(R.id.linear_information_container)
    LinearLayout informationContainerLinearLayout;


    private int fragmentNumber = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupWindowAnimations();

        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            fragmentNumber = savedInstanceState.getInt(STATE_FRAGMENT_OPENED);
            isLoading = savedInstanceState.getBoolean(STATE_LOADING_STARTED);
        }
        switchFragment(fragmentNumber);

        startLoading(R.string.loading_message);
        if (!ZodisPreferences.getDatabaseStatusPref(this)) {
            if(NetworkUtils.isOnline(this)){
            /*Start service to fetch JSON data*/
                _startService();
            }else{
            /*Show no network error message*/
                stopLoading(R.string.no_network_message);
            }
        }else{
            stopLoading();
        }


        if (!ZodisPreferences.getFirstTimePref(this)) {
            ZodisPreferences.saveFirstTimePref(this, true);
            startActivity(new Intent(this, LoginActivity.class));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(ZODIS_SYNC_TASK_SERVICE));

    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
            getWindow().setExitTransition(slide);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_learn:
                fragmentNumber = 0;
                switchFragment(fragmentNumber);
                break;
            case R.id.action_insights:
                fragmentNumber = 1;
                switchFragment(fragmentNumber);
                break;
            case R.id.action_profile:
                fragmentNumber = 2;
                switchFragment(fragmentNumber);
                break;
        }
        return true;
    }

    private void switchFragment(int index) {
        Fragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        switch (index) {
            case 0:
                fragment = manager.findFragmentByTag(TAG_RETAINED_LEVELS_FRAGMENT);
                if (fragment == null) {
                    fragment = LevelsFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment, TAG_RETAINED_LEVELS_FRAGMENT).commit();
                break;
            case 1:
                fragment = manager.findFragmentByTag(TAG_RETAINED_INSIGHTS_FRAGMENT);
                if (fragment == null) {
                    fragment = InsightsFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment, TAG_RETAINED_INSIGHTS_FRAGMENT).commit();
                break;
            default:
                fragment = manager.findFragmentByTag(TAG_RETAINED_USER_FRAGMENT);
                if (fragment == null) {
                    fragment = UserFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment, TAG_RETAINED_USER_FRAGMENT).commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // perform other onPause related actions
        // this means that this activity will not be recreated now, user is leaving it
        // or the activity is otherwise finishing
        if (isFinishing()) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(TAG_RETAINED_USER_FRAGMENT);
            if (fragment != null) {
                manager.beginTransaction().remove(fragment).commit();
            }

            fragment = manager.findFragmentByTag(TAG_RETAINED_INSIGHTS_FRAGMENT);
            if (fragment != null) {
                manager.beginTransaction().remove(fragment).commit();
            }

            fragment = manager.findFragmentByTag(TAG_RETAINED_LEVELS_FRAGMENT);
            if (fragment != null) {
                manager.beginTransaction().remove(fragment).commit();
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_FRAGMENT_OPENED, fragmentNumber);
        savedInstanceState.putBoolean(STATE_LOADING_STARTED, isLoading);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (NetworkUtils.isOnline(this)) {
                    startLoading(R.string.loading_message);
                    _startService();
                }else{
                    Toast.makeText(this, getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void _startService() {
        startService(new Intent(this, ZodisSyncIntentService.class));
    }

    private void startLoading(int message){
        /* User clicked refreshed button*/
        isLoading = true;
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mStatusTextView.setVisibility(View.VISIBLE);
        mStatusTextView.setText(message);
        loadingContainerLinearLayout.setVisibility(View.VISIBLE);
        informationContainerLinearLayout.setVisibility(View.GONE);
    }


    private void stopLoading(){
        /* Data has been fetched */
        isLoading = false;
        loadingContainerLinearLayout.setVisibility(View.GONE);
        informationContainerLinearLayout.setVisibility(View.VISIBLE);
    }

    private void stopLoading(int message){
        /* Some error occurred */
        isLoading = false;
        mLoadingIndicator.setVisibility(View.GONE);
        mStatusTextView.setVisibility(View.VISIBLE);
        mStatusTextView.setText(message);
        loadingContainerLinearLayout.setVisibility(View.VISIBLE);
        informationContainerLinearLayout.setVisibility(View.GONE);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            isLoading = false;
            int  status_code = intent.getIntExtra(SERVICE_RESULT,0);
            switch (status_code) {
                case 0:
                    //error
                    stopLoading(R.string.database_error_message);
                    break;
                case 1:
                    //data retrieved
                    stopLoading();
                    Toast.makeText(context, getString(R.string.databse_updated), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
