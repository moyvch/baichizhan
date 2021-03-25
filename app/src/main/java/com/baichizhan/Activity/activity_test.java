package com.baichizhan.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.word.Mp3;
import com.baichizhan.R;
import com.baichizhan.word.Word;
import com.baichizhan.word.wordRandom;

import java.util.Random;

public class activity_test extends BaseActivity {
    private TextView English, tab1, tab2, tab3, tab4, yingbiao;
    private Button nextBtn;
    private boolean[] btnLimit;
    private int length;
    private int index;
    private Intent intent;
    private SQLiteDatabase database;
    private int answer;
    private int result;
    private Word[] words;
    private Bundle bundle;
    private Handler handler;
    private TextView[] textViews;
    private ImageView returnImg, playImg;
    private Mp3 mp3;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setStatusBar();


        bundle = getIntent().getExtras();
        words = (Word[]) bundle.getSerializable("bundle");
//        MainActivity.instance.finish();
        English = findViewById(R.id.english);
        yingbiao = findViewById(R.id.yingbiao1);

//        选项
        tab1 = findViewById(R.id.textView11);
        tab2 = findViewById(R.id.textView12);
        tab3 = findViewById(R.id.textView13);
        tab4 = findViewById(R.id.textView14);

//        返回建
        returnImg = findViewById(R.id.fhj);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });
//        播放图片
        playImg = findViewById(R.id.fy2);

        handler = new Handler();
        textViews = new TextView[4];
        textViews[0] = tab1;
        textViews[1] = tab2;
        textViews[2] = tab3;
        textViews[3] = tab4;
        btnLimit = new boolean[4];
        btnLimit[0] = true;
        btnLimit[1] = true;
        btnLimit[2] = true;
        btnLimit[3] = true;
        nextBtn = findViewById(R.id.khan);
        length = bundle.getInt("index");
        database = new DatabaseUtil().getSqLiteDatabase();
        index = 0;
        result = 0;


        mp3 = new Mp3();
        update(index);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == length) {
                    nextBtn.setText("完成测试");
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            result();
                        }
                    });
                } else {
                    btnLimit[0] = true;
                    btnLimit[1] = true;
                    btnLimit[2] = true;
                    btnLimit[3] = true;
                    mp3.stopMp3();
                    update(++index);
                    nextBtn.setVisibility(View.INVISIBLE);
                    vcolor();
                }
            }
        });
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabClick(0);
            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabClick(1);
            }
        });
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabClick(2);
            }
        });
        tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabClick(3);
            }
        });

    }
    
//    选项卡点击事件
    public void tabClick(int anwz) {
        if (btnLimit[anwz]) {
            btnLimit[0] = false;
            btnLimit[1] = false;
            btnLimit[2] = false;
            btnLimit[3] = false;
            vcolor();
            if (answer == anwz) {
                textViews[answer].setBackgroundColor(Color.rgb(171, 235, 198));

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("success");
                        ++result;
                        if (index == length) {
                            nextBtn.setText("完成测试");
                            nextBtn.setVisibility(View.VISIBLE);
                            nextBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    result();
                                }
                            });
                        } else {
                            mp3.stopMp3();
                            update(++index);
                            vcolor();
                        }
                        btnLimit[0] = true;
                        btnLimit[1] = true;
                        btnLimit[2] = true;
                        btnLimit[3] = true;
                    }
                }, 450);
            } else {
                textViews[answer].setBackgroundColor(Color.rgb(171, 235, 198));
                textViews[anwz].setBackgroundColor(Color.rgb(250, 128, 114));
                nextBtn.setVisibility(View.VISIBLE);
            }
        }
    }

//    题目刷新
    public void update(int zxcs) {
        playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3.openMp3(words[zxcs].getUrl());
            }
        });
        mp3.openMp3(words[zxcs].getUrl());
        English.setText(words[zxcs].getEnglish());
        yingbiao.setText("/" + words[zxcs].getYingbiao() + "/");
        Random random = new Random();

        Cursor cursor = database.rawQuery("select * from words", null);
        Word[] daos1 = new wordRandom(database).khsj(cursor, 4);
        System.out.println(daos1.length);
        String[] cs = new String[4];

        answer = random.nextInt(3);
        boolean ws = true;
        for (int i = 0; i < daos1.length; i++) {
            cs[i] = daos1[i].getChinese();
            if (daos1[i].getEnglish() != null)
                if (daos1[i].getEnglish().equals(words[zxcs].getEnglish())) {
                    answer = i;
                    ws = false;
                }
        }
        if (ws) {
            cs[answer] = words[zxcs].getChinese();
        }
        tab1.setText("A." + cs[0]);
        tab2.setText("B." + cs[1]);
        tab3.setText("C." + cs[2]);
        tab4.setText("D." + cs[3]);

    }
    
//    清除TextView颜色
    public void vcolor() {
        for (TextView aj : textViews) {
            aj.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

//    测试结果
    public void result() {
        System.out.println("测试结果为" + result + "/" + length);
        Intent csIntent = new Intent(this, activity_testResult.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", words);
        bundle.putInt("zqs", result);
        bundle.putInt("zs", length);
        csIntent.putExtras(bundle);
        startActivity(csIntent);
    }

//    手机返回事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            return true;

            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    沉浸式布局
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white1));//设置状态栏颜色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_test.this);
        builder.setMessage("是否退出考核？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity_test.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}

