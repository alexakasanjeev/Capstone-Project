package com.betatech.alex.zodis.data.pojo;

import java.util.ArrayList;

/**
 * Collection of 10 question
 */

public class QuestionBank {

    private ArrayList<Question> questions;
    private ArrayList<String> possibleAnswersForRoot;
    private ArrayList<String> possibleAnswersForDerived;

    public QuestionBank(ArrayList<Question> questions, ArrayList<String> possibleAnswersForRoot, ArrayList<String> possibleAnswersForDerived) {
        this.questions = questions;
        this.possibleAnswersForRoot = possibleAnswersForRoot;
        this.possibleAnswersForDerived = possibleAnswersForDerived;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getPossibleAnswersForRoot() {
        return possibleAnswersForRoot;
    }

    public void setPossibleAnswersForRoot(ArrayList<String> possibleAnswersForRoot) {
        this.possibleAnswersForRoot = possibleAnswersForRoot;
    }

    public ArrayList<String> getPossibleAnswersForDerived() {
        return possibleAnswersForDerived;
    }

    public void setPossibleAnswersForDerived(ArrayList<String> possibleAnswersForDerived) {
        this.possibleAnswersForDerived = possibleAnswersForDerived;
    }
}
