package com.betatech.alex.zodis.ui.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.pojo.RootWord;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    public static final String KEY_DATA = "DATA";
    private ArrayList<RootWord>  dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (getIntent()!=null) {
            dataList = getIntent().getParcelableArrayListExtra(KEY_DATA);
        }

        if(dataList==null){
            // TODO: 12/4/2017 put all strings in strings.xml
            Toast.makeText(this, "Unable to start Quiz!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
