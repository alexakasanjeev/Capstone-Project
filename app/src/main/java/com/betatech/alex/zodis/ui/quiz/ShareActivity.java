package com.betatech.alex.zodis.ui.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.ui.lesson.LessonActivity;
import com.betatech.alex.zodis.utilities.QuizUtils;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        int lessonId = -1;

        if (getIntent()!=null) {
            lessonId = getIntent().getIntExtra(LessonActivity.KEY_LESSON_ID,-1);
            QuizUtils.increaseXP(this, String.valueOf(lessonId));
        }
    }
}
