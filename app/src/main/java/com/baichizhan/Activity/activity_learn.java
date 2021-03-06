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

//        ???????????????????????????????????????
        data = new NowTime().now();
        studyNum = databaseUtil.dataInt(data,1);
        reviewNum = words.length - studyNum;
        schedule.setText("?????????" + studyNum + "\n" + "?????????" + reviewNum);

//        ?????????????????????

        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });

//        ??????MP3???????????????
        mp3 = new Mp3();
//        ?????????????????????
        index = 0;
        learnUpdate(index);

//       ???????????????????????????
        if (reviewNum > 0) {
            reviewNum -= 1;
        } else {
            reviewNum = 0;
            studyNum = words.length - index - 1;
        }
        schedule.setText("?????????" + studyNum + "\n" + "?????????" + reviewNum);

//      ?????????????????????
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == 0) {
//                    ?????????????????????,???????????????
                    Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
                } else {
//                    ????????????????????????????????????
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

//      ?????????????????????
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtil.changeStau(2,english);
                if (index == words.length - 1) {
//                    ?????????????????????,?????????????????????????????????????????????
                    intent = new Intent(activity_learn.this, activity_learnOver.class);
                    mp3.stopMp3();
                    intentSkip(intent);
                } else {
//                    ????????????????????????????????????
                    index++;
                    playImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp3.openMp3(words[index].getUrl());
                        }
                    });
                    learnUpdate(index);

                    //              ???????????????????????????
                    if (reviewNum > 0) {
                        reviewNum -= 1;
                    } else {
                        reviewNum = 0;
                        studyNum = words.length - index - 1;
                    }
                    schedule.setText("?????????" + studyNum + "\n" + "?????????" + reviewNum);
                }
                if (index == 5) {
                    enterTest.setVisibility(View.VISIBLE);
                }
            }
        });

//        ?????????????????????
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_wordDetails.class);
                Bundle bd = new Bundle();
                Cursor cursor = new DatabaseUtil().select();
                cursor.moveToFirst();
                cursor.move(words[index].getId() - 1);
                bd.putString("QUERY", cursor.getString(1));//??????
                ArrayList<String> data = getStrArr(cursor, 3, 6);
                bd.putStringArrayList("MPARR", data);//0,1->?????????2,3->??????
                bd.putStringArrayList("CHINESEARR", getStrArr(cursor, 7, 18));
                bd.putStringArrayList("LIJUARR", getStrArr(cursor, 19, 28));
                intent.putExtras(bd);
                context.startActivity(intent);
            }
        });

//      ??????????????????????????????
        enterTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, activity_test.class);
                mp3.stopMp3();
                intentSkip(intent);
            }
        });
    }

    //    ??????????????????????????????????????????????????????
    public void intentSkip(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", words);
        bundle.putInt("index", index);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    ???????????????????????????????????????????????????
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

    //  ?????????
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white1));//?????????????????????
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//?????????????????????????????????????????????
        }
    }

    //    ??????????????????????????????
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_learn.this);
        builder.setMessage("?????????????????????");
        builder.setTitle("??????");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity_learn.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("??????", null);
        builder.create().show();
    }
}
