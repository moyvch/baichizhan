package com.baichizhan.word;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.data.NowTime;

import java.util.Random;

public class wordRandom {
    private String English, Chinese, yingbiao;
    private int id, stauts;
    private SQLiteDatabase database;
    private String data = new NowTime().now();
    private String pronouncing;
    private DatabaseUtil databaseUtil = new DatabaseUtil();

    public wordRandom(SQLiteDatabase database) {
        this.database = database;
    }

    public Word[] learnRandom(int xudcs) {
        Cursor cursor = databaseUtil.select(1);
        int cn = cursor.getCount();
        Random random = new Random();
        int[] randomArray = new int[xudcs];
        Word[] words = new Word[randomArray.length];
        int index = 0;
        while (index < randomArray.length) {
            int num = random.nextInt(cn);
            //如果数组中不包含这个元素则赋值给数组
            if (!exist(randomArray, num)) {
                randomArray[index++] = num;
            }
        }
        for (int k = 0; k < randomArray.length; k++) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.move(randomArray[k]);
                English = cursor.getString(cursor.getColumnIndex("english"));

                database.execSQL("update words set Time='" + data + "' where english=\"" + English + "\"");

                words[k] = setWord(cursor);
            } else {
                System.out.println("查询失败");
            }
        }
        cursor.close();
        return words;
    }

    public Word[] reviewRandom(int xxdcs) {
        Cursor cursor = databaseUtil.select(2);
        int cn = cursor.getCount();
        Random random = new Random();
        int[] randomArray = new int[xxdcs];
        Word[] words = new Word[randomArray.length];
        int index = 0;
        while (index < randomArray.length) {
            int num = random.nextInt(cn);
            //如果数组中不包含这个元素则赋值给数组
            if (!exist(randomArray, num)) {
                randomArray[index++] = num;
            }
        }
        //打印
        for (int k = 0; k < randomArray.length; k++) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.move(randomArray[k]);
                English = cursor.getString(cursor.getColumnIndex("english"));


//                database.execSQL("update words set Time='" + data + "' where english=\"" + English + "\"");
//                database.execSQL("update words set stauts='" + 3 + "' where english=\"" + English + "\"");
                databaseUtil.updateTime(English,data);
                databaseUtil.updateStauts(English,3);


//                Chinese = cursor.getString(cursor.getColumnIndex("chind0"));
//                yingbiao = cursor.getString(cursor.getColumnIndex("yb0"));
//                pronouncing = cursor.getString(cursor.getColumnIndex("ybd0"));
//                id = cursor.getInt(cursor.getColumnIndex("id"));
//                stauts = cursor.getInt(cursor.getColumnIndex("stauts"));
//
//
//                Word word = new Word();
//                word.setEnglish(English);
//                word.setChinese(Chinese);
//                word.setId(id);
//                word.setStauts(stauts);
//                word.setYingbiao(yingbiao);
//                word.setMp3(pronouncing);
                words[k] = setWord(cursor);
            } else {
                System.out.println("查询失败");
            }
        }
        cursor.close();
        return words;
    }

    //    考核单词随机
    public Word[] khsj(Cursor cursor, int i) {
        int cn = cursor.getCount();
        Random random = new Random();
        int[] randomArray = new int[i];
        Word[] words = new Word[randomArray.length];
        int index = 0;
        while (index < randomArray.length) {
            int num = random.nextInt(cn);
            //如果数组中不包含这个元素则赋值给数组
            if (!exist(randomArray, num)) {
                randomArray[index++] = num;
            }
        }
        //打印
        for (int k = 0; k < randomArray.length; k++) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.move(randomArray[k]);
                English = cursor.getString(cursor.getColumnIndex("english"));
//                Chinese = cursor.getString(cursor.getColumnIndex("chind0"));
//                yingbiao = cursor.getString(cursor.getColumnIndex("yb0"));
//                pronouncing = cursor.getString(cursor.getColumnIndex("ybd0"));
//                id = cursor.getInt(cursor.getColumnIndex("id"));
//                stauts = cursor.getInt(cursor.getColumnIndex("stauts"));
//
//
//                Word word = new Word();
//                word.setEnglish(English);
//                word.setChinese(Chinese);
//                word.setId(id);
//                word.setStauts(stauts);
//                word.setYingbiao(yingbiao);
//                word.setMp3(pronouncing);
                words[k] = setWord(cursor);
            } else {
                System.out.println("查询失败");
            }
        }
        cursor.close();
        return words;
    }

//    复习和考核单词一起生成
    public Word[] content(int xxdcs, int fxdcs, String rq) {
        this.data = rq;
        Word[] togetherWords;
        Word[] studyWords = learnRandom(xxdcs);
        if (fxdcs != 0) {
            Word[] fxdao = reviewRandom(fxdcs);
            togetherWords = new Word[xxdcs + fxdcs];
            for (int i = 0; i < fxdcs; i++) {
                togetherWords[i] = fxdao[i];
            }
            for (int i = 0; i < xxdcs; i++) {
                togetherWords[i + fxdcs] = studyWords[i];
            }
        } else {
            togetherWords = new Word[xxdcs];
            for (int i = 0; i < xxdcs; i++) {
                togetherWords[i] = studyWords[i];
            }

        }
        return togetherWords;
    }

//    判断是否有重复
    public static boolean exist(int[] arr, int key) {

        for (int i = 0; i < arr.length; i++) {
            //如果相等返回true
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }
//    把数据存到word对象
    public Word setWord(Cursor cursor){
        Chinese = cursor.getString(cursor.getColumnIndex("chind0"));
        yingbiao = cursor.getString(cursor.getColumnIndex("yb0"));
        pronouncing = cursor.getString(cursor.getColumnIndex("ybd0"));
        id = cursor.getInt(cursor.getColumnIndex("id"));
        stauts = cursor.getInt(cursor.getColumnIndex("stauts"));

        Word word = new Word();
        word.setEnglish(English);
        word.setChinese(Chinese);
        word.setId(id);
        word.setStauts(stauts);
        word.setYingbiao(yingbiao);
        word.setMp3(pronouncing);
        return word;
    }
}
