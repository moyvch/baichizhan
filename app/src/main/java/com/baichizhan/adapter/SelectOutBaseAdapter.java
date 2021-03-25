package com.baichizhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baichizhan.R;

/*
* 构造历史记录列表
* */
public class SelectOutBaseAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private String[] data;

    public SelectOutBaseAdapter(Context context, int layout, String[] data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
    }


    @Override
    public int getCount() {
        if (data == null)
            return 0;
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t1;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);
        }
        t1 = convertView.findViewById(R.id.english);

        t1.setText(data[position]);

        return convertView;
    }
}
