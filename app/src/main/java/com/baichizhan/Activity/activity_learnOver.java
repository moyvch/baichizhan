package com.baichizhan.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baichizhan.R;
import com.baichizhan.word.Word;

public class activity_learnOver extends BaseActivity {
    private Button learnBtn, testBtn, returnBtn;
    private int index;
    private Word[] words;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learnover);
        setStatusBar();


        learnBtn = findViewById(R.id.jxxx);
        testBtn = findViewById(R.id.jxkh);
        returnBtn = findViewById(R.id.fhzy);
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt("index", 0);
        words = (Word[]) bundle.getSerializable("bundle");

//        继续学习
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_learnOver.this, activity_learn.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundle", words);
                bundle.putInt("index", index);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        进行考核
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity_learnOver.this, activity_test.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundle", words);
                bundle.putInt("index", index);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity_learnOver.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent(activity_learnOver.this, MainActivity.class);
            startActivity(intent);


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white1));//设置状态栏颜色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }
}
