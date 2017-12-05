package com.betatech.alex.zodis.ui.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.pojo.Question;
import com.betatech.alex.zodis.data.pojo.QuestionBank;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.ui.lesson.LessonActivity;
import com.betatech.alex.zodis.utilities.QuizUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    public static final String KEY_DATA = "DATA";
    private static final String STATE_LESSION_ID = "lesson_id";
    private ArrayList<RootWord>  dataList;

    @BindView(R.id.radio_group_answers) RadioGroup radioGroup;
    @BindView(R.id.button_check) Button buttonCheck;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.text_quiz_question) TextView quizQuestionTextView;
    @BindView(R.id.linear_result_background) LinearLayout resultBackgroundLinearLayout;
    @BindView(R.id.text_quiz_result) TextView questionResultTextView;
    @BindView(R.id.text_correct_answer) TextView correctAnswerTextView;

    private QuestionBank questionBank;
    private Question currentQuestion = null;

    private boolean isAlternate = false;
    private int lessonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ButterKnife.bind(this);

        if (getIntent()!=null) {
            dataList = getIntent().getParcelableArrayListExtra(KEY_DATA);
            lessonId = getIntent().getIntExtra(LessonActivity.KEY_LESSON_ID,-1);
        }

        if(dataList==null){
            // TODO: 12/4/2017 put all strings in strings.xml
            Toast.makeText(this, "Unable to start Quiz!", Toast.LENGTH_SHORT).show();
            finish();
        }

        questionBank = QuizUtils.generateTenQuestions(dataList);
        progressBar.setMax(100);
        radioGroup.setOnCheckedChangeListener(this);


        init();

    }

    private void init() {

        if (questionBank.getQuestions().size()>0) {

            buttonCheck.setEnabled(false);
            enableRadioGroup();
            resultBackgroundLinearLayout.setVisibility(View.GONE);
            buttonCheck.setText(R.string.aq_button_check_message);

            currentQuestion = questionBank.getQuestions().remove(0);
            quizQuestionTextView.setText(getString(R.string.question_frame,currentQuestion.getQuestion()));

            ArrayList<String> answers;
            if (currentQuestion.isRootWord()) {
                answers = questionBank.getPossibleAnswersForRoot();
            }else{
                answers = questionBank.getPossibleAnswersForDerived();
            }

            Collections.shuffle(answers,new Random(System.nanoTime()));

            for (int i = 0; i < 5; i++) {
                ((RadioButton)radioGroup.getChildAt(i)).setText(answers.get(i));
            }
        }else{
            Toast.makeText(this, "DONE!!!!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        buttonCheck.setEnabled(true);
    }

    @OnClick(R.id.button_check)
    public void check(){
        if (isAlternate) {
            if(questionBank.getQuestions().size()==0 && currentQuestion == null){
                Intent intent = new Intent(this,ShareActivity.class);
                intent.putExtra(LessonActivity.KEY_LESSON_ID,lessonId);
                startActivity(intent);
                finish();
            }
            init();
            isAlternate=!isAlternate;
            buttonCheck.setEnabled(false);
            return;
        }
        if (questionBank.getQuestions().size()>0  || currentQuestion !=null) {
            int id = radioGroup.getCheckedRadioButtonId();
            String answer = ((RadioButton)radioGroup.findViewById(id)).getText().toString();



            if (currentQuestion.getAnswer().equals(answer)) {
                progressBar.incrementProgressBy(10);
                currentQuestion=null;
                correctAnswer();
            }else{
                questionBank.getQuestions().add(currentQuestion);
                wrongAnswer(currentQuestion.getAnswer());
            }
            currentQuestion=null;
            radioGroup.clearCheck();
            isAlternate=!isAlternate;

        }
    }

    private void correctAnswer(){
        resultBackgroundLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryRelishDark));
        questionResultTextView.setText(getResources().getString(R.string.correct_message));
        correctAnswerTextView.setVisibility(View.GONE);
        buttonCheck.setText(R.string.aq_button_continue_message);
        questionResultTextView.setVisibility(View.VISIBLE);
        resultBackgroundLinearLayout.setVisibility(View.VISIBLE);
        diableRadioGroup();
    }

    private void wrongAnswer(String answer){
        resultBackgroundLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryKetchupDark));
        questionResultTextView.setText(getResources().getString(R.string.wrong_message));
        correctAnswerTextView.setText(getResources().getString(R.string.right_answer_message,answer));
        buttonCheck.setText(R.string.aq_button_continue_message);
        correctAnswerTextView.setVisibility(View.VISIBLE);
        questionResultTextView.setVisibility(View.VISIBLE);
        resultBackgroundLinearLayout.setVisibility(View.VISIBLE);
        diableRadioGroup();
    }

    @OnClick(R.id.image_button_cancel)
    public void createAlertDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.cancel_dialog_title)
                .setMessage(R.string.cancel_dialog_description)
                .setPositiveButton(R.string.cancel_dialog_positive_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_dialog_negative_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        createAlertDialog();
    }

    private void diableRadioGroup(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioGroup(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_LESSION_ID, lessonId);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
