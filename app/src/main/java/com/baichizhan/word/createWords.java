package com.baichizhan.word;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.baichizhan.Activity.activity_learn;
import com.baichizhan.Activity.activity_test;
import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.data.NowTime;

public class createWords {
    private String data = new NowTime().now();
    private int reviewNum, studyNum;
    private int studyed;
    private Word[] words;
    private Context context;
    private Intent intent;
    private DatabaseUtil databaseUtil = new DatabaseUtil();
    private SQLiteDatabase database = databaseUtil.getSqLiteDatabase();
    private com.baichizhan.word.wordRandom wordRandom = new wordRandom(database);
    private String English, Chinese, yingbiao, mp3;
    private int stauts, id;

    //    单词生成
    public createWords() {

    }

    //    任务单词生成
    public createWords(Context context, int reviewNum, int xxdcs) {
        this.reviewNum = reviewNum;
        this.studyNum = xxdcs;
        this.context = context;

        Cursor cursor = databaseUtil.select(1);

//        随机数对象

        studyed = databaseUtil.dataInt(data,2);
        if (cursor.getCount() > 0) {
//            判断单词库的单词是否都学过
            if (studyed == 0 && databaseUtil.dataInt(data,1)==0) {
//                今日未学习
                words = wordRandom.content(xxdcs, reviewNum, data);
                intentSkip(words);
            } else if (studyed == database.query("words", null, "Time=?", new String[]{data}, null, null, null).getCount()) {
//                今日任务已完成
                exitDialog();
            } else {
//            继续完成今天的任务
                words = Tocontinue();
                intentSkip(words);
            }
        } else {
//            所有单词都学习完成
            exitDialog1();
        }
    }

    //    考核单词生成
    public createWords(Context context, int reviewNum) {
        this.context = context;
        Cursor cursor;
        cursor = databaseUtil.select(2);
        if (cursor.getCount() > 10) {
            intent = new Intent(context, activity_test.class);
            words = wordRandom.khsj(cursor, 10);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bundle", words);
            bundle.putInt("index", 9);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } else {
//            点击考核，发现已学单词小于零
            exitDialog2();
        }
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("今天的学习任务已完成^_^");
        builder.setTitle("提示");
        builder.setNegativeButton("再来一组", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                words = wordRandom.content(10, 0, data);
                intentSkip(words);
            }
        });
        builder.setPositiveButton("复习一遍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                words = review();
                intentSkip(words);
            }
        });
        builder.create().show();
    }

    private void exitDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("该词库已学习完成是否重置题库？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.execSQL("update words set stauts=1;");
                Toast.makeText(context, "重置题库成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("进行复习", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                words = wordRandom.reviewRandom(reviewNum);
            }
        });
        builder.create().show();
    }

    private void exitDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("请先学习再来考核^_^");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    //    页面跳转实现
    public void intentSkip(Word[] words) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", words);
        intent = new Intent(context, activity_learn.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //    复习功能的的实现
    public Word[] review() {
        wordRandom = new wordRandom(database);
        Cursor cursor = databaseUtil.dataCursor(data,2);
        Word[] words;
        cursor.moveToFirst();
        words=creConWord(cursor,0,cursor.getCount());
        return words;
    }

    //继续完成未完成的学习

    public Word[] Tocontinue() {
//        复习单词表
        Cursor reviewCurs = databaseUtil.dataCursor(data,3);
        Cursor studyCurs = databaseUtil.dataCursor(data,1);
        if (reviewCurs.getCount() > 0) {
//            遍历需要复习单词
            words = creConWord(reviewCurs, 0, reviewCurs.getCount());
            if (studyCurs.getCount() > 0) {
                words = creConWord(studyCurs, reviewCurs.getCount(), reviewCurs.getCount() + studyCurs.getCount());
            }
        } else {
            words = creConWord(studyCurs, 0, studyCurs.getCount());
        }
        return words;
    }

    public Word[] creConWord(Cursor cursor, int i, int length) {
        cursor.moveToFirst();
        Word[] words = new Word[length];
        do {
            English = cursor.getString(cursor.getColumnIndex("english"));
            Chinese = cursor.getString(cursor.getColumnIndex("chind0"));
            yingbiao = cursor.getString(cursor.getColumnIndex("yb0"));
            id = cursor.getInt(cursor.getColumnIndex("id"));
            stauts = cursor.getInt(cursor.getColumnIndex("stauts"));
            mp3 = cursor.getString(cursor.getColumnIndex("ybd0"));
            Word word = new Word();
            word.setEnglish(English);
            word.setChinese(Chinese);
            word.setId(id);
            word.setStauts(stauts);
            word.setYingbiao(yingbiao);
            word.setMp3(mp3);
            words[i] = word;
            i++;
        } while (cursor.moveToNext());
        cursor.close();
        return words;
    }

}

