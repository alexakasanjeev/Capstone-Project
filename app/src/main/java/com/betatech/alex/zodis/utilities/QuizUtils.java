package com.betatech.alex.zodis.utilities;

import com.betatech.alex.zodis.data.pojo.DerivedWord;
import com.betatech.alex.zodis.data.pojo.Question;
import com.betatech.alex.zodis.data.pojo.QuestionBank;
import com.betatech.alex.zodis.data.pojo.RootWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
}
