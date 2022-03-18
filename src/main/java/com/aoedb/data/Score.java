package com.aoedb.data;

import java.io.Serializable;

public class Score implements Serializable {
    int numStarted, numFinished;
    int answeredQuestions, correctAnswers, wrongAnswers;
    int score10, score20, score30;
    int correct10, correct20, correct30;
    int streak10, streak20, streak30;
    float multiplier10, multiplier20, multiplier30;

    public Score() {
        numStarted = 0;
        numFinished = 0;
        answeredQuestions = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
        score10 = score20 = score30 = 0;
        correct10 = correct20 = correct30 = 0;
        streak10 = streak20 = streak30 = 0;
        multiplier10 = multiplier20 = multiplier30 = 1;
    }

    public int getNumStarted() {
        return numStarted;
    }

    public void setNumStarted(int numStarted) {
        this.numStarted = numStarted;
    }

    public int getNumFinished() {
        return numFinished;
    }

    public void setNumFinished(int numFinished) {
        this.numFinished = numFinished;
    }

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getScore(int questions){
        switch (questions){
            case 10: return score10;
            case 20: return score20;
            case 30: return score30;
            default: return -1;
        }
    }

    public void setScore(int questions, int num){
        switch (questions){
            case 10: this.score10 = num; break;
            case 20: this.score20 = num; break;
            case 30: this.score30 = num; break;
        }
    }

    public int getCorrectAnswers(int questions){
        switch (questions){
            case 10: return correct10;
            case 20: return correct20;
            case 30: return correct30;
            default: return -1;
        }
    }

    public void setCorrectAnswers(int questions, int num){
        switch (questions){
            case 10: this.correct10 = num; break;
            case 20: this.correct20 = num; break;
            case 30: this.correct30 = num; break;
        }
    }

    public String writeCorrectAnswers(int questions){
        switch (questions){
            case 10: return correct10+"/10";
            case 20: return correct20+"/20";
            case 30: return correct30+"/30";
            default: return "";
        }
    }

    public int getStreak(int questions){
        switch (questions){
            case 10: return streak10;
            case 20: return streak20;
            case 30: return streak30;
            default: return -1;
        }
    }

    public void setStreak(int questions, int num){
        switch (questions){
            case 10: this.streak10 = num; break;
            case 20: this.streak20 = num; break;
            case 30: this.streak30 = num; break;
        }
    }

    public float getMultiplier(int questions){
        switch (questions){
            case 10: return multiplier10;
            case 20: return multiplier20;
            case 30: return multiplier30;
            default: return -1;
        }
    }

    public void setMultiplier(int questions, float num){
        switch (questions){
            case 10: this.multiplier10 = num; break;
            case 20: this.multiplier20 = num; break;
            case 30: this.multiplier30 = num; break;
        }
    }
}
