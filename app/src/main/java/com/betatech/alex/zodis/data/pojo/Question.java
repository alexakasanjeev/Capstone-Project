package com.betatech.alex.zodis.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents single question Entity asked in QuizActivity
 */

public class Question implements Parcelable{

    private String question;
    private String answer;
    private boolean isRootWord;

    public Question(String question, String answer, boolean isRootWord) {
        this.question = question;
        this.answer = answer;
        this.isRootWord = isRootWord;
    }

    private void readFromParcel(Parcel in) {
        question = in.readString();
        answer = in.readString();
        isRootWord = in.readByte() != 0;
    }

    private Question(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }


    public String getAnswer() {
        return answer;
    }


    public boolean isRootWord() {
        return isRootWord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeByte((byte) (isRootWord ? 1 : 0));
    }
}
