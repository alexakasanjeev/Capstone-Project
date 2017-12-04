package com.betatech.alex.zodis.ui.quiz;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.pojo.Question;
import com.betatech.alex.zodis.data.pojo.QuestionBank;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.utilities.QuizUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    public static final String KEY_DATA = "DATA";
    private ArrayList<RootWord>  dataList;

    @BindView(R.id.radio_group_answers) RadioGroup radioGroup;
    @BindView(R.id.button_check) Button buttonCheck;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.text_quiz_question) TextView quizQuestionTextView;

    private QuestionBank questionBank;
    private int questionNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ButterKnife.bind(this);

        if (getIntent()!=null) {
            dataList = getIntent().getParcelableArrayListExtra(KEY_DATA);
        }

        if(dataList==null){
            // TODO: 12/4/2017 put all strings in strings.xml
            Toast.makeText(this, "Unable to start Quiz!", Toast.LENGTH_SHORT).show();
            finish();
        }

        questionBank = QuizUtils.generateTenQuestions(dataList);



        init();

    }

    private void init() {
        buttonCheck.setEnabled(true);
        buttonCheck.setText("Check");
        Question question = questionBank.getQuestions().get(questionNumber);
        quizQuestionTextView.setText(question.getQuestion());
        ArrayList<String> answers;
        if (question.isRootWord()) {
            answers = questionBank.getPossibleAnswersForRoot();
        }else{
            answers = questionBank.getPossibleAnswersForDerived();
        }

        Collections.shuffle(answers,new Random(System.nanoTime()));

        for (int i = 0; i < 5; i++) {
            ((RadioButton)radioGroup.getChildAt(i)).setText(answers.get(i));
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        buttonCheck.setEnabled(true);
    }

    @OnClick(R.id.button_check)
    public void check(){
        int id = radioGroup.getCheckedRadioButtonId();
        String answer = ((RadioButton)radioGroup.findViewById(id)).getText().toString();

        Question question = questionBank.getQuestions().get(questionNumber);

        if (question.getAnswer().equals(answer)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            progressBar.incrementProgressBy(progressBar.getProgress()+10);
        }else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }

        questionNumber++;
        init();
    }
}
