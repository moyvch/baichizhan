package com.baichizhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baichizhan.R;

import java.util.List;
import java.util.Map;

/*
* 构造搜索结果列表
* */
public class SelectBaseAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Map<String, String>> data;

    public SelectBaseAdapter(Context context, int layout, List<Map<String, String>> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
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
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);
        }
        t1 = convertView.findViewById(R.id.english);
        t2 = convertView.findViewById(R.id.chinses);

        t1.setText(data.get(position).get("ENGLISH"));
        t2.setText(data.get(position).get("CHINSES"));

        return convertView;
    }
}
