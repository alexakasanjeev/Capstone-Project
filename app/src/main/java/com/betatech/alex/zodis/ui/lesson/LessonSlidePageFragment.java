package com.betatech.alex.zodis.ui.lesson;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/4/2017.
 */

public class LessonSlidePageFragment extends Fragment{

    private static final String KEY_ROOT_WORD = "root_word";
    private RootWord rootWord;

    @BindView(R.id.text_root_word) TextView rootWordTextView;
    @BindView(R.id.text_description_word) TextView rootWordDescriptionTextView;
    @BindView(R.id.recycler_derived_words) RecyclerView derivedList;

    public LessonSlidePageFragment() {
    }

    public static LessonSlidePageFragment newInstance(RootWord rootWord) {
        LessonSlidePageFragment fragment = new LessonSlidePageFragment();
        Bundle bundle =new Bundle();
        bundle.putParcelable(LessonSlidePageFragment.KEY_ROOT_WORD,rootWord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            rootWord = getArguments().getParcelable(KEY_ROOT_WORD);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_lesson_slide_page, container, false);
        ButterKnife.bind(this,rootView);

        if (rootWord!=null) {
            rootWordTextView.setText(rootWord.getRootWordName());
            rootWordDescriptionTextView.setText(rootWord.getRootWordDescription());
        }

        DerivedWordAdapter mAdapter = new DerivedWordAdapter(getActivity(), rootWord.getDerivedWordList());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(derivedList.getContext(),
                manager.getOrientation());
        derivedList.addItemDecoration(dividerItemDecoration);
        derivedList.setLayoutManager(manager);
        derivedList.setAdapter(mAdapter);


        return rootView;
    }

}
