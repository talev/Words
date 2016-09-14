package com.talev.words;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dimko Talev on 15.07.2016 г.
 */
public class Word implements Serializable {

    private boolean isLearned = false;
    private String word;
    private String wordTranslated;
    private Date date;

    public Word(String word1, String word2) {
        this.word = word1;
        this.wordTranslated = word2;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordTranslated() {
        return wordTranslated;
    }

    public void setWordTranslated(String wordTranslated) {
        this.wordTranslated = wordTranslated;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
