package com.betatech.alex.zodis.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.sync.ScheduleNotification;
import com.betatech.alex.zodis.sync.ZodisSyncIntentService;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsFragment;
import com.betatech.alex.zodis.ui.tabs.levels.LevelsFragment;
import com.betatech.alex.zodis.ui.tabs.user.UserFragment;
import com.betatech.alex.zodis.utilities.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.top_navigation) Toolbar mToolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        /* Open level Fragment on start*/
        switchFragment(0);



        /*if (getIntent()!=null) {
            String x = getIntent().getStringExtra("XYZ");
            name.setText(x);
        }*/

        if (!ZodisPreferences.getDatabaseStatusPref(this)) {
            startService(new Intent(this,ZodisSyncIntentService.class));
        }else {
            Cursor x =getContentResolver().query(ZodisContract.RootEntry.CONTENT_URI,null,null,null,null);
            Cursor y =getContentResolver().query(ZodisContract.DerivedEntry.CONTENT_URI,null,null,null,null);

            if (x!=null && x.getCount()>0) {
                Toast.makeText(this, ""+x.getCount(), Toast.LENGTH_SHORT).show();
            }

            if (y!=null && y.getCount()>0) {
                Toast.makeText(this, ""+ y.getCount(), Toast.LENGTH_SHORT).show();
            }
        }

        if (!ZodisPreferences.getFirstTimePref(this)) {
            ZodisPreferences.saveDatabaseStatusPref(this,true);
            ScheduleNotification.start(this,20);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_learn:
                switchFragment(0);
                break;
            case R.id.action_insights:
                switchFragment(1);
                break;
            case R.id.action_profile:
                switchFragment(2);
                break;
        }
        return true;
    }

    private void switchFragment(int index) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = LevelsFragment.newInstance();
                break;
            case 1:
                fragment = InsightsFragment.newInstance();
                break;
            default:
                 fragment = UserFragment.newInstance();
        }
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }

}
