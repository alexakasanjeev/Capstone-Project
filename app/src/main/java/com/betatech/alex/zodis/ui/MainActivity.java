package com.betatech.alex.zodis.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.sync.ScheduleNotification;
import com.betatech.alex.zodis.sync.ZodisSyncIntentService;
import com.betatech.alex.zodis.ui.login.LoginActivity;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsFragment;
import com.betatech.alex.zodis.ui.tabs.levels.LevelsFragment;
import com.betatech.alex.zodis.ui.tabs.user.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG_RETAINED_USER_FRAGMENT = "user_fragment";
    private static final String TAG_RETAINED_INSIGHTS_FRAGMENT = "insights_fragment";
    private static final String TAG_RETAINED_LEVELS_FRAGMENT = "levels_fragment";
    private static final String STATE_FRAGMENT_OPENED = "fragment_number";
    @BindView(R.id.am_toolbar) Toolbar mToolbar;
    @BindView(R.id.am_bottomNavigation) BottomNavigationView mBottomNavigation;

    private int fragmentNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            fragmentNumber = savedInstanceState.getInt(STATE_FRAGMENT_OPENED);
        }
        switchFragment(fragmentNumber);



        /*if (getIntent()!=null) {
            String x = getIntent().getStringExtra("XYZ");
            name.setText(x);
        }*/

        if (!ZodisPreferences.getDatabaseStatusPref(this)) {
            startService(new Intent(this,ZodisSyncIntentService.class));
        }else {
            Cursor x =getContentResolver().query(ZodisContract.RootEntry.CONTENT_URI,null,null,null,null);
            Cursor y =getContentResolver().query(ZodisContract.DerivedEntry.CONTENT_URI,null,null,null,null);
            Cursor z =getContentResolver().query(ZodisContract.LevelEntry.CONTENT_URI,null,null,null,null);

            if (z!=null) {
                Toast.makeText(this, ""+z.getCount(), Toast.LENGTH_SHORT).show();
            }

        }

        if (!ZodisPreferences.getFirstTimePref(this)) {
            ZodisPreferences.saveFirstTimePref(this,true);
            startActivity(new Intent(this, LoginActivity.class));
            ScheduleNotification.start(this,20);
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
                fragmentNumber =1;
                switchFragment(fragmentNumber);
                break;
            case R.id.action_profile:
                fragmentNumber =2;
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
                if (fragment==null) {
                    fragment = LevelsFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment,TAG_RETAINED_LEVELS_FRAGMENT).commit();
                break;
            case 1:
                fragment = manager.findFragmentByTag(TAG_RETAINED_INSIGHTS_FRAGMENT);
                if (fragment==null) {
                    fragment = InsightsFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment,TAG_RETAINED_INSIGHTS_FRAGMENT).commit();
                break;
            default:
                fragment = manager.findFragmentByTag(TAG_RETAINED_USER_FRAGMENT);
                if (fragment==null) {
                    fragment = UserFragment.newInstance();
                }
                manager.beginTransaction().replace(R.id.am_fragment_container, fragment,TAG_RETAINED_USER_FRAGMENT).commit();
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

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }

}
