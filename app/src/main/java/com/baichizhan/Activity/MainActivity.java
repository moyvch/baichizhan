package com.baichizhan.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.tasks.LearnTasks;
import com.baichizhan.R;
import com.baichizhan.word.createWords;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {
    private LinearLayout select;
    private TextView xxrw1, xxrw2;
    private ConstraintLayout zj;
    private ImageView queryList;
    private Button button;
    private Context context = MainActivity.this;

    private TextView zi;
    private TextView mu;
    private TextView t1;
    private TextView t2;

    //SharedPreferences数据库
    private SharedPreferences sp;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //第一次打开时拷贝数据库
        sp = getSharedPreferences("setting", MODE_PRIVATE);
        sp.getBoolean("one", false);
        if (sp.getBoolean("one", true)) {
            try {
                copyData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sp.edit().putBoolean("one", false).commit();
        }

        //单词列表跳转
        queryList = findViewById(R.id.queryList);
        queryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_wordList.class);
                startActivity(intent);
            }
        });

        //        学习任务
        xxrw1 = findViewById(R.id.xxdcs);
        xxrw2 = findViewById(R.id.fxdcs);
        LearnTasks learntasks = new LearnTasks(context);
        int xxdcs = learntasks.getLearnNum();
        int fxdcs = learntasks.getReviewNum();
        xxrw1.setText(xxdcs + "");
        xxrw2.setText(fxdcs + "");

        //学习进度条
        zi = findViewById(R.id.zi);
        mu = findViewById(R.id.mu);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t1.post(new Runnable() {
            @Override
            public void run() {
                changeWordCount();
            }
        });

        //背单词按钮
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWords createWords = new createWords(context, fxdcs, xxdcs);
            }
        });

        //        考核
        zj = findViewById(R.id.zj);
        zj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createWords(context, 10);
            }
        });

        select = findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_select.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    /*
    * 拷贝数据库
    * 将raw目录下数据库拷贝到本机内部储存，以供操作
    * */
    private void copyData() throws IOException {
        String dbName = "book.db";// 数据库的名字
        String DATABASE_PATH = "/data/data/com.baichizhan/databases/";// 数据库在手机里的路径
        String databaseFilenames = DATABASE_PATH + dbName;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
            dir.mkdir();
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
        InputStream is = this.getResources().openRawResource(R.raw.book);// 得到数据库文件的数据流
        byte[] buffer = new byte[8192];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }

    //搜索后单词数量增加，主页面更新数据
    private void changeWordCount() {
        String ziStr = String.valueOf(new DatabaseUtil().select(2).getCount());
        zi.setText(ziStr);
        String muStr = String.valueOf(new DatabaseUtil().select().getCount());
        mu.setText(muStr);
        int width = (int) ((Double.parseDouble(ziStr) / Double.parseDouble(muStr)) * t2.getWidth());
        t1.setWidth(width);
    }

    @Override//页面返回时更新单词数据
    protected void onResume() {
        changeWordCount();
        super.onResume();
    }
}
