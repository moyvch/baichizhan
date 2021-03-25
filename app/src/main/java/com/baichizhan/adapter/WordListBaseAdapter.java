package com.baichizhan.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baichizhan.api.API_Util;
import com.baichizhan.Activity.activity_wordDetails;
import com.baichizhan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.baichizhan.database.DatabaseUtil.getStrArr;

/*
* 构造单词列表
* */
public class WordListBaseAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Map<String, String>> data;
    private Cursor cursor;
    private List<Integer> list = new ArrayList<>();//点击隐藏释义保存position，重新构造时保留原样式

    public WordListBaseAdapter(Context context, int layout, List<Map<String, String>> data, Cursor cursor) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t1;
        TextView t2;
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);
        }
        t1 = convertView.findViewById(R.id.english);
        t2 = convertView.findViewById(R.id.chinses);
        imageView = convertView.findViewById(R.id.xq);

        t1.setText(data.get(position).get("ENGLISH"));
        t2.setText(data.get(position).get("CHINSES"));

        //用回收池中数据构造列表项时不被原数据样式影响
        t1.setTextColor(context.getResources().getColor(R.color.black));
        if (list.contains(position)) {
            t2.setTextColor(context.getResources().getColor(R.color.ja));
            t2.setBackgroundColor(context.getResources().getColor(R.color.ia));
//            System.out.println("显示"+list);
        }else{
            t2.setTextColor(context.getResources().getColor(R.color.ia));
            t2.setBackgroundColor(context.getResources().getColor(R.color.ch));
//            System.out.println("遮住"+list);
        }

        //点击播放单词读音，默认美式
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setTextColor(context.getResources().getColor(R.color.jo));

                //方法一，高效
//                cursor.moveToFirst();
//                cursor.move(position);
//                if (cursor.getString(4) != null)
//                    API_Util.start_V(cursor.getString(4));
//
                //方法二，兼容
                API_Util.v_api(data.get(position).get("ENGLISH"));
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t1.setTextColor(context.getResources().getColor(R.color.black));
                }).start();
            }
        });

        //点击显示隐藏单词释义
        t2.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = v.getBackground();
                ColorDrawable dra = (ColorDrawable) drawable;
//                System.out.println(position);
                if (dra.getColor() == context.getResources().getColor(R.color.ia)) {
                    v.setBackgroundColor(context.getResources().getColor(R.color.ch));
                    t2.setTextColor(context.getResources().getColor(R.color.ia));
                    if (list.contains(position)){
                        list.removeAll(Arrays.asList(position));
//                        System.out.println("删除后："+list);
                    }
                } else {//显示
                    v.setBackgroundColor(context.getResources().getColor(R.color.ia));
                    t2.setTextColor(context.getResources().getColor(R.color.ja));
                    if (!list.contains(position)){
                        list.add(position);
//                        System.out.println("添加后："+list);
                    }
                }
            }
        });

        //跳转单词详情业
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_wordDetails.class);
                Bundle bd = new Bundle();
                cursor.moveToPosition(position);
                bd.putString("QUERY", cursor.getString(1));//单词
                ArrayList<String> data = getStrArr(cursor, 3, 6);
                bd.putStringArrayList("MPARR", data);//0,1->英式，2,3->美式
                bd.putStringArrayList("CHINESEARR", getStrArr(cursor, 7, 18));
                bd.putStringArrayList("LIJUARR", getStrArr(cursor, 19, 28));
                intent.putExtras(bd);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
