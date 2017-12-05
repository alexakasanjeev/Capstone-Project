package com.betatech.alex.zodis.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.data.pojo.DerivedWord;
import com.betatech.alex.zodis.data.pojo.Question;
import com.betatech.alex.zodis.data.pojo.QuestionBank;
import com.betatech.alex.zodis.data.pojo.RootWord;
import com.betatech.alex.zodis.widget.ZodisWidgetService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by lenovo on 12/5/2017.
 */

public class QuizUtils {

    public static QuestionBank generateTenQuestions(ArrayList<RootWord> list){
        Random rand = new Random(System.nanoTime());


        ArrayList<Question> allQuestions = new ArrayList<>();
        ArrayList<String> possibleAnswersForRoot = new ArrayList<>();
        ArrayList<String> possibleAnswersForDerived = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            RootWord rootWord = list.get(i);
            Question rootQuestion = new Question(rootWord.getRootWordName(),rootWord.getRootWordDescription(),true);
            possibleAnswersForRoot.add(rootQuestion.getAnswer());

            int randomNum = randInt(rand,0,rootWord.getDerivedWordList().size()-1);

            ArrayList<DerivedWord>  derivedWordsList = rootWord.getDerivedWordList();

            Question derivedQuestion = new Question(derivedWordsList.get(randomNum).getDerivedWordName(),derivedWordsList.get(randomNum).getDerivedWordDescription(),false);
            possibleAnswersForDerived.add(derivedQuestion.getAnswer());

            allQuestions.add(rootQuestion);
            allQuestions.add(derivedQuestion);
        }


        Collections.shuffle(possibleAnswersForRoot,new Random());
        Collections.shuffle(possibleAnswersForDerived,new Random());

        return new QuestionBank(allQuestions,possibleAnswersForRoot,possibleAnswersForDerived);

    }

    public static int randInt(Random rand,int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static void increaseXP(Context context,String lessonId){

            String selections = ZodisContract.LevelEntry.COLUMN_LESSON_ID + " =?";
            String[] selectionArgs = new String[]{lessonId};
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZodisContract.LevelEntry.COLUMN_LEVEL_STATUS,1);
            context.getContentResolver().update(ZodisContract.LevelEntry.CONTENT_URI,contentValues,selections,selectionArgs);
            ZodisPreferences.incrementXpPref(context);
            ZodisWidgetService.startActionUpdateAllWidgets(context);

    }
}
