package com.talev.words;

import java.io.Serializable;

/**
 * Created by Dimko Talev on 15.07.2016 г.
 */
public class Word implements Serializable {

    private String word1;
    private String word2;

    public Word(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }
}
