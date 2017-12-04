package com.betatech.alex.zodis.ui.lesson;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.betatech.alex.zodis.data.pojo.RootWord;

import java.util.ArrayList;

/**
 * PageAdapter for ViewPager, to unable user to scroll through root word
 */

public class LessonSlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<RootWord> rootList;

    public LessonSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return LessonSlidePageFragment.newInstance(rootList.get(position));
    }

    @Override
    public int getCount() {
        return rootList!=null?rootList.size():0;
    }

    public void swapList(ArrayList<RootWord> list){
        if (rootList!=null) {
            rootList = null;
        }

        rootList = list;
        notifyDataSetChanged();
    }
}
