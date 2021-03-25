package com.baichizhan.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baichizhan.R;
import com.baichizhan.word.Word;

public class activity_testResult extends BaseActivity {
    private TextView string1, string2;
    private Button againBtn, returnBtn;
    private int zqs, zs;
    private String xs1, xs2;
    private Word[] words;
    public static activity_testResult instance = null;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testresult);
        instance = this;
        setStatusBar();
        string1 = findViewById(R.id.textView16);
        string2 = findViewById(R.id.textView17);
        againBtn = findViewById(R.id.againBtn);
        returnBtn = findViewById(R.id.returnBtn);
        Bundle bundle = getIntent().getExtras();
        zqs = bundle.getInt("zqs", 0);
        zs = bundle.getInt("zs", 0);
        words = (Word[]) bundle.getSerializable("bundle");
        for (Word a : words) {
            System.out.println(a.getChinese());
        }
        result(zqs, zs + 1);
        string1.setText(xs1);
        string2.setText(xs2);

        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity_testResult.this, activity_test.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundle", words);
                bundle.putInt("index", zs);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity_testResult.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

//    结果显示
    public void result(int a, int b) {
        int bl;
        if (a % b == 0) {
            bl = a / b * 100;
        } else {
            bl = new Double((double) Math.round(a / (double) b * 1000) / 10).intValue();
        }
        System.out.println(bl);
        if (bl == 100) {
            xs1 = "恭喜你全对";
            xs2 = "大神的操作真的6";
            string1.setTextColor(Color.rgb(36, 9, 207));
        } else if (bl > 90) {
            xs1 = "成绩优秀";
            xs2 = "你的正确率高达" + bl + "%" + "离大神就差一步";
            string1.setTextColor(Color.rgb(142, 68, 173));
        } else if (bl > 80) {
            xs1 = "成绩良好";
            xs2 = "你的正确率高达" + bl + "%" + "加油向优秀靠齐";
            string1.setTextColor(Color.rgb(46, 204, 113));
        } else if (bl > 60) {
            xs1 = "成绩合格";
            xs2 = "你的正确率高达" + bl + "%" + "加油向良好靠齐";
            string1.setTextColor(Color.rgb(243, 156, 18));
        } else if (bl < 60) {
            xs1 = "不及格";
            xs2 = "不会吧正确率才" + bl + "%" + "有点扣脚呢";
            string1.setTextColor(Color.rgb(241, 30, 30));
        }
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white1));//设置状态栏颜色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent(activity_testResult.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
