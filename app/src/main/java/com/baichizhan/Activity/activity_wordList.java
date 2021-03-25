package com.baichizhan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.adapter.WordListBaseAdapter;
import com.baichizhan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 单词列表界面类
* */
public class activity_wordList extends BaseActivity {
    private ListView listView;
    private ImageView center;

    //单词分类按钮
    private TextView z;
    private TextView y;
    private TextView s;

    private TextView queryCount;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        //内容延申到状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }

        center = findViewById(R.id.center);
        listView = findViewById(R.id.listView);
        z = findViewById(R.id.z);
        y = findViewById(R.id.y);
        s = findViewById(R.id.s);
        queryCount = findViewById(R.id.queryCount);

        //所有单词列表
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> data = getData(0);
                listView.setAdapter(new WordListBaseAdapter(activity_wordList.this, R.layout.querylist_item, data, cursor));
                z.setBackground(getResources().getDrawable(R.drawable.buttonbar));
                y.setBackgroundColor(getResources().getColor(R.color.ia));
                s.setBackgroundColor(getResources().getColor(R.color.ia));
                queryCount.setText("单词数：" + data.size());
            }
        });
        //已学单词列表
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> data = getData(2);
                listView.setAdapter(new WordListBaseAdapter(activity_wordList.this, R.layout.querylist_item, data, cursor));
                z.setBackgroundColor(getResources().getColor(R.color.ia));
                y.setBackground(getResources().getDrawable(R.drawable.buttonbar));
                s.setBackgroundColor(getResources().getColor(R.color.ia));
                queryCount.setText("单词数：" + data.size());
            }
        });
        //未学单词列表
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> data = getData(1);
                listView.setAdapter(new WordListBaseAdapter(activity_wordList.this, R.layout.querylist_item, data, cursor));
                z.setBackgroundColor(getResources().getColor(R.color.ia));
                y.setBackgroundColor(getResources().getColor(R.color.ia));
                s.setBackground(getResources().getDrawable(R.drawable.buttonbar));
                queryCount.setText("单词数：" + data.size());
            }
        });

        //返回
        center.setOnClickListener(v -> {
            this.finish();
        });

        //默认显示所有单词列表
        List<Map<String, String>> data = getData(0);
        queryCount.setText("单词数：" + data.size());
        listView.setAdapter(new WordListBaseAdapter(this, R.layout.querylist_item, data, cursor));
    }

    //设置状态栏字体颜色
    //暂未使用
    public void changStatusIconCollor(boolean setDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (setDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    /*
    * 获取单词数据
    * 根据传入status，查询数据库中status项的值，1->未学 2->已学 0->所有
    * */
    private List<Map<String, String>> getData(int status) {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map;

        if (status == 0)
            cursor = new DatabaseUtil().select();
        else
            cursor = new DatabaseUtil().select(status);

        if (cursor == null || cursor.getCount() == 0)
            return data;

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            map = new HashMap<>();
            map.put("ENGLISH", cursor.getString(1));
            map.put("CHINSES", (cursor.getString(7) + cursor.getString(8) + cursor.getString(9)
                    + cursor.getString(10) + cursor.getString(11) + cursor.getString(12) + cursor.getString(13)
                    + cursor.getString(14) + cursor.getString(15) + cursor.getString(16) + cursor.getString(17)
                    + cursor.getString(18)).replace("null", ""));
            data.add(map);
            cursor.moveToNext();
        }
        return data;
    }
}