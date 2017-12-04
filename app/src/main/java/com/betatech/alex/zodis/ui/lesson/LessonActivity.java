package com.betatech.alex.zodis.ui.lesson;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.ui.quiz.QuizActivity;
import com.betatech.alex.zodis.utilities.GeneralUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // TODO: 12/4/2017 Save state for orientation chnage

    public static final String TAG = LessonActivity.class.getSimpleName();
    private int currentPosition = 0;

    /**
     *  Lesson ID KEY
     */
    public static final String KEY_LESSON_ID = "lesson_id";
    public static final String KEY_ROOT_WORD_ID = "root_word";
    public static final String KEY_LEVEL_NAME = "level_name";

    private static final int ROOT_LOADER_ID = 123;
    private static final int DERIVED_LOADER_ID = 456;

    private Cursor rootWordCursor = null;
    private ArrayList<RootWord> list=null;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next root word.
     */
    @BindView(R.id.pager) ViewPager mPager;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_button_left) ImageButton leftButton;
    @BindView(R.id.image_button_right) ImageButton rightButton;
    @BindView(R.id.button_start_quiz) Button quizButton;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private LessonSlidePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        int lessonId =-1;
        String levelName ="";
        if (getIntent()!=null) {
            lessonId = getIntent().getIntExtra(KEY_LESSON_ID,-1);
            levelName =getIntent().getStringExtra(KEY_LEVEL_NAME).trim();
        }

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(levelName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Toast.makeText(this, levelName, Toast.LENGTH_SHORT).show();
        mToolbar.setTitle(getString(R.string.show_level_name,levelName));

        if (lessonId==-1) {
            /*Wrong lesson Id */finish();
        }else{
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_LESSON_ID,lessonId);
            getSupportLoaderManager().initLoader(ROOT_LOADER_ID,bundle,this);
        }

        mPagerAdapter = new LessonSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position>0) {
                    leftButton.setEnabled(true);
                }else{
                    leftButton.setEnabled(false);
                }

                if(position==rootWordCursor.getCount()-1){
                    rightButton.setEnabled(false);
                    quizButton.setEnabled(true);
                }else{
                    rightButton.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        leftButton.setEnabled(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection;
        String selection;
        String[] selectionsArgs;

        switch (id) {
            case ROOT_LOADER_ID:
                projection=new String[]{ZodisContract.RootEntry._ID, ZodisContract.RootEntry.COLUMN_NAME, ZodisContract.RootEntry.COLUMN_DESCRIPTION};
                selection = ZodisContract.RootEntry.COLUMN_LESSON + "=?";
                selectionsArgs = new String[]{String.valueOf(args.getInt(KEY_LESSON_ID))};
                return new CursorLoader(this, ZodisContract.RootEntry.CONTENT_URI,projection,selection,selectionsArgs,null);
            case DERIVED_LOADER_ID:

                int argcount = rootWordCursor.getCount(); // number of IN arguments
                selectionsArgs = new String[argcount];
                StringBuilder inList = new StringBuilder(argcount * 2);
                for (int i = 0; i < argcount; i++) {
                    if(i > 0) {
                        inList.append(",");
                    }
                    inList.append("?");
                    rootWordCursor.moveToPosition(i);
                    selectionsArgs[i]=rootWordCursor.getString(rootWordCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_NAME));
                }
                projection = new String[]{ZodisContract.DerivedEntry.COLUMN_NAME, ZodisContract.DerivedEntry.COLUMN_DESCRIPTION, ZodisContract.DerivedEntry.COLUMN_ROOT_NAME};
                selection = ZodisContract.DerivedEntry.COLUMN_ROOT_NAME + " IN ("+inList.toString()+")";

                return new CursorLoader(this, ZodisContract.DerivedEntry.CONTENT_URI,projection,selection,selectionsArgs,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ROOT_LOADER_ID:
                if (data!=null && data.getCount()>0) {
                    rootWordCursor = data;
                    getSupportLoaderManager().initLoader(DERIVED_LOADER_ID,null,this);
                }else{
                    // TODO: 12/4/2017 make it correct
                    Log.d(TAG, "onLoadFinished: Some Error");
                }
                break;
            case DERIVED_LOADER_ID:
                if (data!=null && data.getCount()>0 && rootWordCursor!=null) {
                    list = new ArrayList<>();
                    for (int i = 0; i < rootWordCursor.getCount(); i++) {
                        list.add(GeneralUtils.extractWords(rootWordCursor,data,i));
                    }
                    mPagerAdapter.swapList(list);
                }else{
                    // TODO: 12/4/2017 make it correct
                    Log.d(TAG, "onLoadFinished: Some Error");
                }
                break;
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.image_button_left)
    public void swipeLeft(){
        //it doesn't matter if you're already in the first item
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    @OnClick(R.id.image_button_right)
    public void swipeRight(){
        //it doesn't matter if you're already in the last item
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    @OnClick(R.id.button_start_quiz)
    public void startQuizActivity(){
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putParcelableArrayListExtra(QuizActivity.KEY_DATA,list);
        startActivity(intent);
        finish();
    }
}
