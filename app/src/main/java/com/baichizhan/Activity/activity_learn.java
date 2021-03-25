package com.baichizhan.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.word.Mp3;
import com.baichizhan.data.NowTime;
import com.baichizhan.R;
import com.baichizhan.word.Word;

import java.util.ArrayList;

import static com.baichizhan.database.DatabaseUtil.getStrArr;

public class activity_learn extends BaseActivity {
    private Button preBtn, nextBtn, detailsBtn;
    private TextView englishText, chineseText, enterTest, yingbiao, schedule;
    private ImageView returnImg, playImg;
    private Intent intent;
    private Bundle bundle;
    private SQLiteDatabase database;
    public Context context;
    public int index,id;
    private int studyNum, reviewNum;
    private String phoneticSy;
    private String english, chinese;
    private String data;
    private Word[] words;
    private Mp3 mp3;
    private DatabaseUtil databaseUtil;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        setStatusBar();
        context=this;

        databaseUtil=new DatabaseUtil();
        preBtn = findViewById(R.id.preBtn);
        nextBtn = findViewById(R.id.nextBtn);
        detailsBtn = findViewById(R.id.detailsBtn);
        englishText = findViewById(R.id.English);
        chineseText = findViewById(R.id.Chinese);
        enterTest = findViewById(R.id.enterTest);
        yingbiao = findViewById(R.id.yingbiao);
        returnImg = findViewById(R.id.returnImg);
        schedule = findViewById(R.id.schedule);
        playImg = findViewById(R.id.fy1);


        database = databaseUtil.getSqLiteDatabase();


        bundle = getIntent().getExtras();
        words = (Word[]) bundle.getSerializable("bundle");

//        待学和待过数值显示功能实现
        data = new NowTime().now();
        studyNum = databaseUtil.dataInt(data,1);
        reviewNum = words.length - studyNum;
        schedule.setText("待学：" + studyNum + "\n" + "待过：" + reviewNum);

//        返回键功能实现

        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });

//        创建MP3播放器对象
        mp3 = new Mp3();
//        第一个单词加载
        index = 0;
        learnUpdate(index);

//       待过和待学数值更新
        if (reviewNum > 0) {
            reviewNum -= 1;
        } else {
            reviewNum = 0;
            studyNum = words.length - index - 1;
        }
        schedule.setText("待学：" + studyNum + "\n" + "待过：" + reviewNum);

//      上一题功能实现
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == 0) {
//                    在第一个单词时,点击上一个
                    Toast.makeText(context, "上面没有了", Toast.LENGTH_SHORT).show();
                } else {
//                    上一个单词显示并播放音频
                    index--;
                    playImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp3.openMp3(words[index].getUrl());
                        }
                    });
                    learnUpdate(index);
                }
            }
        });

//      下一题功能实现
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtil.changeStau(2,english);
                if (index == words.length - 1) {
//                    最后一个单词时,点下一个进行页面跳转并暂停音频
                    intent = new Intent(activity_learn.this, activity_learnOver.class);
                    mp3.stopMp3();
                    intentSkip(intent);
                } else {
//                    下一个单词显示并播放音频
                    index++;
                    playImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp3.openMp3(words[index].getUrl());
                        }
                    });
                    learnUpdate(index);

                    //              待过和待学数值更新
                    if (reviewNum > 0) {
                        reviewNum -= 1;
                    } else {
                        reviewNum = 0;
                        studyNum = words.length - index - 1;
                    }
                    schedule.setText("待学：" + studyNum + "\n" + "待过：" + reviewNum);
                }
                if (index == 5) {
                    enterTest.setVisibility(View.VISIBLE);
                }
            }
        });

//        下一个功能实现
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_wordDetails.class);
                Bundle bd = new Bundle();
                Cursor cursor = new DatabaseUtil().select();
                cursor.moveToFirst();
                cursor.move(words[index].getId() - 1);
                bd.putString("QUERY", cursor.getString(1));//单词
                ArrayList<String> data = getStrArr(cursor, 3, 6);
                bd.putStringArrayList("MPARR", data);//0,1->英式，2,3->美式
                bd.putStringArrayList("CHINESEARR", getStrArr(cursor, 7, 18));
                bd.putStringArrayList("LIJUARR", getStrArr(cursor, 19, 28));
                intent.putExtras(bd);
                context.startActivity(intent);
            }
        });

//      直接进行考核功能实现
        enterTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, activity_test.class);
                mp3.stopMp3();
                intentSkip(intent);
            }
        });
    }

    //    将数据传到结果页面中去并进行页面跳转
    public void intentSkip(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", words);
        bundle.putInt("index", index);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    更新单词和单词意思，并播放对应音频
    public void learnUpdate(int index){
        mp3.stopMp3();
        mp3.openMp3(words[index].getUrl());
        english = words[index].getEnglish();
        chinese = words[index].getChinese();
        id = words[index].getId();
        phoneticSy = words[index].getYingbiao();
        englishText.setText(english);
        chineseText.setText(chinese);
        yingbiao.setText("/" + phoneticSy + "/");
    }

    //  沉浸式
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white1));//设置状态栏颜色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    //    手机自己的返回键设置
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_learn.this);
        builder.setMessage("是否退出学习？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity_learn.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
