package com.baichizhan.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.baichizhan.data.NowTime;
import com.baichizhan.database.DatabaseUtil;

public class LearnTasks {
    private SQLiteDatabase database;
    private int reviewNum, learnNum, learnedNum, learnedNow;
    private Cursor cursor;
    private String data;
    private Context context;
    private DatabaseUtil databaseUtil = new DatabaseUtil();

    public LearnTasks() {

    }

    public LearnTasks(Context context) {
        this.context = context;

        database = databaseUtil.getSqLiteDatabase();

        //        获取当前日期
        NowTime nowTime = new NowTime();
        data = nowTime.now();
//        数据库的初始化
        database.execSQL("update words set stauts=" + 1 + " where stauts=" + 3 + " and Time!='" + data + "'");
//        数据库已完成的单词数，判断是否要复习
        learnedNum = databaseUtil.select(2).getCount();
//        未学习单词的表
        cursor = databaseUtil.select(1);

//            今天完成的单词数
        learnedNow = databaseUtil.dataInt(data,2);

        learnPlan();
    }

    //    学习计划制定，复习单词数和学习单词数的确定
    public void learnPlan() {
//        判断任务是否生成
        if (database.query("words", null, "Time=?", new String[]{data}, null, null, null).getCount() > 0) {
            reviewNum = databaseUtil.dataInt(data,3);
            learnNum = databaseUtil.dataInt(data,1);
            System.out.println(learnNum);
        } else {
            if (cursor.getCount() > 10) {
//                未学习的单词数大于10时
                learnNum = 10;
                reviewNum = 5;
                if (learnedNum < 5) {
//                且已学习的单词小于五时
                    reviewNum = learnedNum;
                    learnNum = 15 - learnedNum;
                }
            } else if (cursor.getCount() > 0 & cursor.getCount() < 10) {
//                未学习的单词数小于10大于0时
                learnNum = cursor.getCount();
                reviewNum = 15 - learnNum;
            } else if (cursor.getCount() == 0) {
//                词库的单词已经学完
                learnNum = 0;
                reviewNum = 15;
                Toast.makeText(context, "词库的单词已经学完", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public int getLearnNum() {
        return learnNum;
    }

}
