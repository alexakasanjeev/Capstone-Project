package com.betatech.alex.zodis.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Collection of 10 question
 */

public class QuestionBank implements Parcelable{

    private ArrayList<Question> questions;
    private ArrayList<String> possibleAnswersForRoot;
    private ArrayList<String> possibleAnswersForDerived;

    public QuestionBank(ArrayList<Question> questions, ArrayList<String> possibleAnswersForRoot, ArrayList<String> possibleAnswersForDerived) {
        this.questions = questions;
        this.possibleAnswersForRoot = possibleAnswersForRoot;
        this.possibleAnswersForDerived = possibleAnswersForDerived;
    }

    protected QuestionBank(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        questions = in.createTypedArrayList(Question.CREATOR);
        possibleAnswersForRoot = in.createStringArrayList();
        possibleAnswersForDerived = in.createStringArrayList();
    }

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(questions);
        dest.writeStringList(possibleAnswersForRoot);
        dest.writeStringList(possibleAnswersForDerived);
    }
}
