package com.betatech.alex.zodis.ui.lesson;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.utilities.GeneralUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = LessonActivity.class.getSimpleName();

    /**
     *  Lesson ID KEY
     */
    public static final String KEY_LESSON_ID = "lesson_id";

    private static final int LOADER_ID = 123;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next root word.
     */
    @BindView(R.id.pager) ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private LessonScreenSlidePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        ButterKnife.bind(this);

        int lessonId =-1;
        if (getIntent()!=null) {
            lessonId = getIntent().getIntExtra(KEY_LESSON_ID,-1);
        }

        if (lessonId==-1) {
            /*Wrong lesson Id */finish();
        }else{
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_LESSON_ID,lessonId);
            getSupportLoaderManager().initLoader(LOADER_ID,bundle,this);
        }

        mPagerAdapter = new LessonScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection=new String[]{ZodisContract.RootEntry._ID, ZodisContract.RootEntry.COLUMN_NAME, ZodisContract.RootEntry.COLUMN_DESCRIPTION};
        String selection = ZodisContract.RootEntry.COLUMN_LESSON + "=?";
        String[] selectionsArgs = new String[]{String.valueOf(args.getInt(KEY_LESSON_ID))};
        return new CursorLoader(this, ZodisContract.RootEntry.CONTENT_URI,projection,selection,selectionsArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null && data.getCount()>0) {
            ArrayList<RootWord> list = new ArrayList<>();
            for (int i = 0; i < data.getCount(); i++) {
                list.add(GeneralUtils.extractRootWord(data,i));
            }
            mPagerAdapter.swapList(list);
        }else{
            // TODO: 12/4/2017 make it correct
            Log.d(TAG, "onLoadFinished: Some Error");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
