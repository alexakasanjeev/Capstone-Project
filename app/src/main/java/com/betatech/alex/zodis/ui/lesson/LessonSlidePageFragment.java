package com.betatech.alex.zodis.ui.lesson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/4/2017.
 */

public class LessonSlidePageFragment extends Fragment {

    private static final String KEY_ROOT_WORD = "root_word";
    private RootWord rootWord;

    @BindView(R.id.xyz) TextView xyz;

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
            xyz.setText(rootWord.getRootWordName());
        }

        return rootView;
    }
}
