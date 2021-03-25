package com.baichizhan.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
* 数据库操作类
* */
public class DatabaseUtil {
    private SQLiteDatabase sqLiteDatabase;
    private String table = "words";

    //设置要操作的表
    public void setTable(String table) {
        this.table = table;
    }

    //初始化SQLiteDatabase对象
    public DatabaseUtil() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/com.baichizhan/databases/book.db", null);
    }

    //初始化SQLiteDatabase对象并指定操作的表
    public DatabaseUtil(String table) {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/com.baichizhan/databases/book.db", null);
        this.table = table;
    }

    //添加
    public void insert(List<ArrayList<String>> list) {
        ContentValues cValues = new ContentValues();
        cValues.put("english", list.get(0).get(0));

        if (list.get(1).size() > 0) {
            cValues.put("yb0", list.get(1).get(0));
            cValues.put("ybd0", list.get(1).get(1));
            if (list.get(1).size() > 2) {
                cValues.put("yb1", list.get(1).get(2));
                cValues.put("ybd1", list.get(1).get(3));
            }
        }

        if (list.get(2).size() > 0) {
            cValues.put("chin0", list.get(2).get(0));
            cValues.put("chind0", list.get(2).get(1));
            if (list.get(2).size() > 2) {
                cValues.put("chin1", list.get(2).get(2));
                cValues.put("chind1", list.get(2).get(3));
                if (list.get(2).size() > 4) {
                    cValues.put("chin2", list.get(2).get(4));
                    cValues.put("chind2", list.get(2).get(5));
                    if (list.get(2).size() > 6) {
                        cValues.put("chin3", list.get(2).get(6));
                        cValues.put("chind3", list.get(2).get(7));
                        if (list.get(2).size() > 8) {
                            cValues.put("chin4", list.get(2).get(8));
                            cValues.put("chind4", list.get(2).get(9));
                            if (list.get(2).size() > 10) {
                                cValues.put("chin5", list.get(2).get(10));
                                cValues.put("chind5", list.get(2).get(11));
                            }
                        }
                    }
                }
            }
        }

        if (list.get(3).size() > 0) {
            cValues.put("lj0", list.get(3).get(0));
            cValues.put("ljd0", list.get(3).get(1));
            if (list.get(3).size() > 2) {
                cValues.put("lj1", list.get(3).get(2));
                cValues.put("ljd1", list.get(3).get(3));
                if (list.get(3).size() > 4) {
                    cValues.put("lj2", list.get(3).get(4));
                    cValues.put("ljd2", list.get(3).get(5));
                    if (list.get(3).size() > 6) {
                        cValues.put("lj3", list.get(3).get(6));
                        cValues.put("ljd3", list.get(3).get(7));
                        if (list.get(3).size() > 8) {
                            cValues.put("lj4", list.get(3).get(8));
                            cValues.put("ljd4", list.get(3).get(9));
                        }
                    }
                }
            }
        }

        sqLiteDatabase.insert(table, null, cValues);
    }

    //清空历史记录数据表
    public void delectAll() {
        sqLiteDatabase.execSQL("DELETE from out;");
    }

    //查询所有单词
    public Cursor select() {
        Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null);
        return cursor;
    }

    //查询某状态单词的数量
    public Cursor select(int z) {
        Cursor cursor = sqLiteDatabase.query(table, null, "stauts = " + z, null, null, null, null);
        return cursor;
    }

    //单词模糊查询前10条记录
    public Cursor select(String columns) {
        String item;
        //判断是否含有汉字
        if (columns.length() == columns.getBytes().length) {
            item = "English";
        } else {
            item = "chind0";
        }
        String str = "select * from " + table + " where " + item + " like ? limit 10;";
        Cursor cursor = sqLiteDatabase.rawQuery(str, new String[]{columns + "%"});
        return cursor;
    }

    //    查询xx日期学习状态的表
    public Cursor select(int stauts, String rq) {
        Cursor cursor = sqLiteDatabase.query("words", null, "stauts=? and Time='?'", new String[]{stauts + "", rq}, null, null, null);
        return cursor;
    }


    //    某日已完成的单词的数量
    public int dataInt(String nowData, int stauts) {
        Cursor cursor = sqLiteDatabase.query("words", null, "Time='" + nowData + "' and stauts=" + stauts, null, null, null, null);
        return cursor.getCount();
    }

    public Cursor dataCursor(String nowData, int stauts) {
        Cursor cursor = sqLiteDatabase.query("words", null, "Time='" + nowData + "' and stauts=" + stauts, null, null, null, null);
        return cursor;
    }

    public void changeStau(int stauts,String english){
        sqLiteDatabase.execSQL("update words set stauts="+2+" where english=\"" + english + "\";");
    }

    //获取cursor的a（包含）到b（包含）列的数据
    public static ArrayList<String> getStrArr(Cursor cursor, int a, int b) {
        ArrayList<String> list = new ArrayList<>();
        while (a <= b) {
            if (a >= 7 && a <= 17 && cursor.getString(a) == null && cursor.getString(a + 1) != null) {
                list.add(" ");
                System.out.println(cursor.getString(a) + "," + a);
            }

            if (cursor.getString(a) != null) {
                list.add(cursor.getString(a));
            }
            a++;
        }
        return list;
    }

    public void updateStauts(String English, int stauts){
        sqLiteDatabase.execSQL("update words set stauts=" + stauts + " where english=\"" + English + "\"");
    }

    public void updateTime(String English,String Time){
        sqLiteDatabase.execSQL("update words set Time='" + Time + "' where english=\"" + English + "\"");
    }

    //获取sqLiteDatabase对象
    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

}
