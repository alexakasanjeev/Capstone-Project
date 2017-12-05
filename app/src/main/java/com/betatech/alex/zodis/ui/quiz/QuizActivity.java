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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    public static final String KEY_DATA = "DATA";
    private static final String STATE_QUESTION_BANK = "question_bank";
    private static final String STATE_GAME = "game_state";
    private static final String STATE_CORRECT_OR_NOT = "correct_or_not";
    private ArrayList<RootWord>  dataList;

    @BindView(R.id.radio_group_answers) RadioGroup radioGroup;
    @BindView(R.id.button_check) Button checkButton;
    @BindView(R.id.button_continue) Button continueButton;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.text_quiz_question) TextView quizQuestionTextView;
    @BindView(R.id.linear_result_background) LinearLayout resultBackgroundLinearLayout;
    @BindView(R.id.text_quiz_result) TextView questionResultTextView;
    @BindView(R.id.text_correct_answer) TextView correctAnswerTextView;

    private QuestionBank questionBank;
    /*
        * State 0 =  User hasn't clicked any RadioButton => Disable checkButton
        * Stage 1 =  User has clicked RadioButton but not checkButton =>  Enable checkButton
        * Stage 2 =  User has clicked checkButton => Show Correct or wrong message
     */
    private int gameState = 0;
    /*
        * whetherUserWasCorrectOrNot = -1 => answer hasn't been evaluated
        * whetherUserWasCorrectOrNot = 0  => answer has been evaluated and it is wrong
        * whetherUserWasCorrectOrNot = 1  => answer has been evaluated and it is right
    */
    private int whetherUserWasCorrectOrNot = -1;
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

        progressBar.setMax(100);
        radioGroup.setOnCheckedChangeListener(this);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            questionBank = savedInstanceState.getParcelable(STATE_QUESTION_BANK);
            gameState = savedInstanceState.getInt(STATE_GAME);
            whetherUserWasCorrectOrNot = savedInstanceState.getInt(STATE_CORRECT_OR_NOT);

        }else{
            questionBank = QuizUtils.generateTenQuestions(dataList);
        }

        resultBackgroundLinearLayout.setVisibility(View.GONE);

        init();

    }

    private void init() {
        if (questionBank.getQuestions().size()>0) {

            Question question = questionBank.getQuestions().get(0);
            quizQuestionTextView.setText(getString(R.string.question_frame, question.getQuestion()));

            ArrayList<String> answers;
            if (question.isRootWord()) {
                answers = questionBank.getPossibleAnswersForRoot();
            }else{
                answers = questionBank.getPossibleAnswersForDerived();
            }

            for (int i = 0; i < 5; i++) {
                ((RadioButton)radioGroup.getChildAt(i)).setText(answers.get(i));
            }

            enableDisableViews(question);
        }


    }

    private void enableDisableViews(Question question) {
        resetViews();
        switch (gameState) {
            case 0:
                checkButton.setEnabled(false);
                break;
            case 1:
                break;
            case 2:
                checkButton.setVisibility(View.GONE);
                continueButton.setVisibility(View.VISIBLE);
                switch (whetherUserWasCorrectOrNot) {
                    case 0:
                        wrongAnswer(question.getAnswer());
                        break;
                    case 1:
                        correctAnswer();
                }
                break;
        }
    }

    private void resetViews() {
        enableRadioGroup();
        resultBackgroundLinearLayout.setVisibility(View.GONE);
        checkButton.setEnabled(true);
        checkButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        checkButton.setEnabled(true);
        if(gameState == 0)
            gameState = 1;

    }

    @OnClick(R.id.button_continue)
    public void nextQuestion(){
        radioGroup.clearCheck();
        if (questionBank.getQuestions().size()>0) {
            Question previousQuestion =questionBank.getQuestions().remove(0);
            if (whetherUserWasCorrectOrNot==0) {
                questionBank.getQuestions().add(previousQuestion);
            }
            if (questionBank.getQuestions().size()==0) {
                finishQuiz();
            }
            whetherUserWasCorrectOrNot = -1;
            gameState = 0;
            init();
        }else{
            finishQuiz();
        }
    }

    @OnClick(R.id.button_check)
    public void check(){
        boolean isAnyOneChecked = false;
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            isAnyOneChecked = ((RadioButton) radioGroup.getChildAt(i)).isChecked();
            if (isAnyOneChecked) {
                break;
            }
        }

        if (!isAnyOneChecked) {
            return;
        }

        gameState = 2;

        int id = radioGroup.getCheckedRadioButtonId();
        String userAnswer = ((RadioButton)radioGroup.findViewById(id)).getText().toString();
        Question question = questionBank.getQuestions().get(0);

        if (question.getAnswer().equals(userAnswer)) {
            progressBar.incrementProgressBy(10);
            whetherUserWasCorrectOrNot =1;
        }else{
            whetherUserWasCorrectOrNot = 0;
        }
        enableDisableViews(question);


    }

    private void correctAnswer(){
        resultBackgroundLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryRelishDark));
        questionResultTextView.setText(getResources().getString(R.string.correct_message));
        correctAnswerTextView.setVisibility(View.GONE);
        resultBackgroundLinearLayout.setVisibility(View.VISIBLE);
        disableRadioGroup();
    }

    private void wrongAnswer(String answer){
        resultBackgroundLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryKetchupDark));
        questionResultTextView.setText(getResources().getString(R.string.wrong_message));
        correctAnswerTextView.setText(getResources().getString(R.string.right_answer_message,answer));
        checkButton.setText(R.string.aq_button_continue_message);
        correctAnswerTextView.setVisibility(View.VISIBLE);
        resultBackgroundLinearLayout.setVisibility(View.VISIBLE);
        disableRadioGroup();
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

    private void disableRadioGroup(){
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
        savedInstanceState.putParcelable(STATE_QUESTION_BANK, questionBank);
        savedInstanceState.putInt(STATE_GAME, gameState);
        savedInstanceState.putInt(STATE_CORRECT_OR_NOT, whetherUserWasCorrectOrNot);




        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void finishQuiz(){
        Intent intent = new Intent(this,ShareActivity.class);
        intent.putExtra(LessonActivity.KEY_LESSON_ID,lessonId);
        startActivity(intent);
        finish();
    }

}
