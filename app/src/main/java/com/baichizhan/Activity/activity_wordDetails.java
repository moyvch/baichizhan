package com.baichizhan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baichizhan.api.API_Util;
import com.baichizhan.R;

import java.util.ArrayList;

import static com.baichizhan.api.API_Util.mediaPlayer;

/*
* 单词详情页
* */
public class activity_wordDetails extends BaseActivity {
    //单词
    private TextView query;

    //英标读音
    private LinearLayout ying;
    private TextView b1;
    private ImageView b1m;
    private TextView b2;
    private ImageView b2m;

    //释义
    private TextView c1q;
    private TextView c1w;
    private TextView c2q;
    private TextView c2w;
    private TextView c3q;
    private TextView c3w;
    private TextView c4q;
    private TextView c4w;
    private TextView c5q;
    private TextView c5w;
    private TextView c6q;
    private TextView c6w;

    //例句
    private LinearLayout y1;
    private LinearLayout y2;
    private LinearLayout y3;
    private LinearLayout y4;
    private LinearLayout y5;
    private LinearLayout y6;
    private TextView d1e;
    private TextView d1c;
    private TextView d2e;
    private TextView d2c;
    private TextView d3e;
    private TextView d3c;
    private TextView d4e;
    private TextView d4c;
    private TextView d5e;
    private TextView d5c;
    private ImageView dv1;
    private ImageView dv2;
    private ImageView dv3;
    private ImageView dv4;
    private ImageView dv5;

    //返回
    private ImageView center;

    //发音网址
    private String url1;
    private String url2;

    //传入数据
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        query = findViewById(R.id.query);

        ying = findViewById(R.id.ying);
        b1 = findViewById(R.id.b1);
        b1m = findViewById(R.id.b1m);
        b2 = findViewById(R.id.b2);
        b2m = findViewById(R.id.b2m);

        c1q = findViewById(R.id.c1q);
        c1w = findViewById(R.id.c1w);
        c2q = findViewById(R.id.c2q);
        c2w = findViewById(R.id.c2w);
        c3q = findViewById(R.id.c3q);
        c3w = findViewById(R.id.c3w);
        c4q = findViewById(R.id.c4q);
        c4w = findViewById(R.id.c4w);
        c5q = findViewById(R.id.c5q);
        c5w = findViewById(R.id.c5w);
        c6q = findViewById(R.id.c6q);
        c6w = findViewById(R.id.c6w);

        y1 = findViewById(R.id.y1);
        y2 = findViewById(R.id.y2);
        y3 = findViewById(R.id.y3);
        y4 = findViewById(R.id.y4);
        y5 = findViewById(R.id.y5);
        y6 = findViewById(R.id.y6);

        d1e = findViewById(R.id.d1e);
        d1c = findViewById(R.id.d1c);
        d2e = findViewById(R.id.d2e);
        d2c = findViewById(R.id.d2c);
        d3e = findViewById(R.id.d3e);
        d3c = findViewById(R.id.d3c);
        d4e = findViewById(R.id.d4e);
        d4c = findViewById(R.id.d4c);
        d5e = findViewById(R.id.d5e);
        d5c = findViewById(R.id.d5c);

        dv1 = findViewById(R.id.dv1);
        dv2 = findViewById(R.id.dv2);
        dv3 = findViewById(R.id.dv3);
        dv4 = findViewById(R.id.dv4);
        dv5 = findViewById(R.id.dv5);

        center = findViewById(R.id.center);


        /*
        * 1. 获取传入数据，为当前页面元素赋值
        * 2. 逻辑判断，根据集合的size选择控件的显示隐藏，布局中默认隐藏，有数据则显示。
        *
        * */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        query.setText(bundle.getString("QUERY", "未读取到数据"));

        data = bundle.getStringArrayList("MPARR");
        if (data.size() > 0) {
            b1.setText("/" + data.get(0) + "/");
            url1 = data.get(1);
            //朗读
            b1m.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    API_Util.start_V(url1);
                }
            });
            ying.setVisibility(View.VISIBLE);
            if (data.size() > 2) {
                b2.setText("/" + data.get(2) + "/");
                url2 = data.get(3);
                b2m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API_Util.start_V(url2);
                    }
                });
                b2m.setVisibility(View.VISIBLE);
            }
        }

        data = bundle.getStringArrayList("CHINESEARR");
        if (data.size() > 0) {
            c1q.setText(data.get(0));
            c1w.setText(data.get(1));
            y1.setVisibility(View.VISIBLE);
            if (data.size() > 2) {
                c2q.setText(data.get(2));
                c2w.setText(data.get(3));
                y2.setVisibility(View.VISIBLE);
                if (data.size() > 4) {
                    c3q.setText(data.get(4));
                    c3w.setText(data.get(5));
                    y3.setVisibility(View.VISIBLE);
                    if (data.size() > 6) {
                        c4q.setText(data.get(6));
                        c4w.setText(data.get(7));
                        y4.setVisibility(View.VISIBLE);
                        if (data.size() > 8) {
                            c5q.setText(data.get(8));
                            c5w.setText(data.get(9));
                            y5.setVisibility(View.VISIBLE);
                            if (data.size() > 10) {
                                c6q.setText(data.get(10));
                                c6w.setText(data.get(11));
                                y6.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }

        data = bundle.getStringArrayList("LIJUARR");
        if (data.size() > 0) {
            d1e.setText(data.get(0));
            d1c.setText(data.get(1));
            dv1.setVisibility(View.VISIBLE);
            if (data.size() > 2) {
                d2e.setText(data.get(2));
                d2c.setText(data.get(3));
                dv2.setVisibility(View.VISIBLE);
                if (data.size() > 4) {
                    d3e.setText(data.get(4));
                    d3c.setText(data.get(5));
                    dv3.setVisibility(View.VISIBLE);
                    if (data.size() > 6) {
                        d4e.setText(data.get(6));
                        d4c.setText(data.get(7));
                        dv4.setVisibility(View.VISIBLE);
                        if (data.size() > 8) {
                            d5e.setText(data.get(8));
                            d5c.setText(data.get(9));
                            dv5.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

        //调用发音API
        dv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_Util.v_api(data.get(0));
            }
        });
        dv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_Util.v_api(data.get(2));
            }
        });
        dv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_Util.v_api(data.get(4));
            }
        });
        dv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_Util.v_api(data.get(6));
            }
        });
        dv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_Util.v_api(data.get(8));
            }
        });


        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_wordDetails.this.finish();
            }
        });

    }

    //页面注销时停止播放
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}