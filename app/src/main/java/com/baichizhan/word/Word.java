package com.baichizhan.word;

import java.io.Serializable;

public class Word implements Serializable {
    private String English, Chinese, yingbiao;
    private int id, stauts;
    private String mp3;

    public Word() {

    }

    public String getChinese() {
        return Chinese;
    }

    public void setChinese(String chinese) {
        Chinese = chinese;
    }

    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public String getYingbiao() {
        return yingbiao;
    }

    public void setYingbiao(String yingbiao) {
        this.yingbiao = yingbiao;
    }


    public String getUrl() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
