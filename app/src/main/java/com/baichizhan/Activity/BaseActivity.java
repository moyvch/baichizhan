package com.baichizhan.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }


    public View getContentViews() {
        FrameLayout view = findViewById(android.R.id.content);
        return view.getChildAt(0);
    }

    protected View contentViews;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
