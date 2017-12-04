package com.betatech.alex.zodis.data.pojo;

/**
 * Represents single question Entity asked in QuizActivity
 */

public class Question {

    private String question;
    private String answer;
    private boolean isRootWord;

    public Question(String question, String answer, boolean isRootWord) {
        this.question = question;
        this.answer = answer;
        this.isRootWord = isRootWord;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRootWord() {
        return isRootWord;
    }

    public void setRootWord(boolean rootWord) {
        isRootWord = rootWord;
    }
}
