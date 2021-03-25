package com.baichizhan.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;

import com.baichizhan.R;

public class activity_Welcome extends BaseActivity {

    private static final int times = 2000;

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_Welcome.this, MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                finish();
            }
        }, times);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
