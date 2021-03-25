package com.baichizhan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baichizhan.api.API_Util;
import com.baichizhan.database.DatabaseUtil;
import com.baichizhan.adapter.SelectBaseAdapter;
import com.baichizhan.adapter.SelectOutBaseAdapter;
import com.baichizhan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baichizhan.api.API_Util.loadxml;
import static com.baichizhan.database.DatabaseUtil.getStrArr;

/*
* 查询界面类
* */
public class activity_select extends BaseActivity implements SearchView.OnQueryTextListener {
    private Button cancel;
    private SearchView searchView;
    private ListView selectListView;
    private TextView clear_out;
    private EditText editText;//搜索框中的输入框

    private Cursor cursor = null;
    private List<String[]> rStr;//网路查词数据存储

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        searchView = findViewById(R.id.searchView);
        cancel = findViewById(R.id.cancel);
        selectListView = findViewById(R.id.selectListView);
        clear_out = findViewById(R.id.clear_out);

        //搜索框样式
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        editText = searchView.findViewById(id);
        editText.setTextSize(14);

        /*
        * 查询单词历史记录表
        * 如果有数据则构造历史记录列表，并显示清空记录按钮
        * */
        String[] outData = getOutData();
        if (outData != null)
            if (outData.length != 0) {
                selectListView.setAdapter(new SelectOutBaseAdapter(this, R.layout.select_out, outData));
                clear_out.setVisibility(View.VISIBLE);
            }
        //清空历史记录按钮事件，清空历史记录表，隐藏listview和清空按钮
        clear_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatabaseUtil("out").delectAll();
                clear_out.setVisibility(View.GONE);
                selectListView.setAdapter(null);
            }
        });

        /*
        * 1. 单词列表项点击事件，进入单词详情业
        * 2. 以cursor值判断当前查询的是本地还是网络，分别处理
        * */
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item项点击事件

                //本地词库
                if (cursor != null) {
                    cursor.moveToPosition(position);

                    List<ArrayList<String>> data = new ArrayList<>();
                    data.add(new ArrayList<String>(Arrays.asList(cursor.getString(1))));
                    data.add(getStrArr(cursor, 3, 6));
                    data.add(getStrArr(cursor, 7, 18));
                    data.add(getStrArr(cursor, 19, 28));

                    if (jump(data)){
                        DatabaseUtil databaseUtil = new DatabaseUtil("out");
                        Cursor c = databaseUtil.select(data.get(0).get(0));
                        if (c.getCount() == 0) {
                            databaseUtil.insert(data);
                        }
                    }

                //网络词库
                } else {
                    new Thread(() -> {
                        List<ArrayList<String>> data = loadxml(rStr.get(position)[0]);

                        DatabaseUtil databaseUtil = new DatabaseUtil();
                        Cursor c = databaseUtil.select(data.get(0).get(0));

                        if (jump(data)) {
                            if (c.getCount() == 0) {
                                databaseUtil.insert(data);
                            }
                            databaseUtil.setTable("out");
                            c = databaseUtil.select(data.get(0).get(0));
                            if (c.getCount() == 0) {
                                databaseUtil.insert(data);
                            }
                        }

                    }).start();
                }
            }
        });

        //事件注册
        searchView.requestFocus();//进入页面搜索框得到焦点
        searchView.setOnQueryTextListener(this);//搜索框内容改变事件和搜索点击事件
        //取消按钮点击事件，注销当前页面
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_select.this.finish();
            }
        });
    }

    //跳转详情页
    private boolean jump(List<ArrayList<String>> data){
        Intent intent = new Intent(activity_select.this, activity_wordDetails.class);
        Bundle bd = new Bundle();
        bd.putString("QUERY", data.get(0).get(0));//单词
        bd.putStringArrayList("MPARR", data.get(1));//0,1->英式，2,3->美式
        bd.putStringArrayList("CHINESEARR", data.get(2));
        bd.putStringArrayList("LIJUARR", data.get(3));
        intent.putExtras(bd);
        activity_select.this.startActivity(intent);
        return true;
    }


    //获取历史记录数据，历史记录在out表，将查询结果返回，前期构造listview使用String[] 懒得改，可改为集合，更优。
    private String[] getOutData() {
        String[] data = null;
        DatabaseUtil databaseUtil = new DatabaseUtil("out");
        cursor = databaseUtil.select();

        if (cursor == null || cursor.getCount() == 0)
            return data;
        cursor.moveToFirst();
        String d = new String();
        for (int i = 0; i < 10; i++) {

            d += (d.length() == 0 ? "" : ",") + cursor.getString(1);

            if (!cursor.moveToNext())
                break;
        }

        if (d != null)
            data = d.split(",");
        return data;
    }

    /*//网络查词线程   翻译，暂时不用
    private boolean urlQuery(String str) {

        if (str.equals(""))
            return false;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String rStr = API_Util.t_api(str);
                        if (rStr == null) {
                            Looper.prepare();
                            Toast.makeText(activity_select.this, "查询失败，检查拼写是否正确或尝试切换端口", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        } else {
                            List<Map<String, String>> data = new ArrayList<>();
                            Map<String, String> map = new HashMap<>();
                            if (str.length() == str.getBytes().length) {//true为英
                                map.put("ENGLISH", str);
                                map.put("CHINSES", rStr);
                                new DatabaseUtil().insert(str, rStr);//加入到数据库
                            } else {
                                map.put("ENGLISH", rStr);
                                map.put("CHINSES", str);
                                new DatabaseUtil().insert(rStr, str);//加入到数据库
                            }
                            data.add(map);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectListView.setAdapter(new MyBaseAdapter(activity_select.this, R.layout.select_item, data));
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }*/

    /*
    * 网络查词线程
    * 调用金山查词API，用查询结果重构listview
    * */
    private boolean urlQuerys(String str) {

        if (str.equals(""))
            return false;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        rStr = API_Util.l_api(str);
                        if (rStr == null) {
                            Looper.prepare();
                            Toast.makeText(activity_select.this, "查询失败，检查拼写是否正确或尝试切换端口", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        } else {
                            List<Map<String, String>> data = new ArrayList<>();
                            Map<String, String> map;
                            for (int i = 0; i < rStr.size(); i++) {
                                map = new HashMap<>();
                                map.put("ENGLISH", rStr.get(i)[0]);
                                map.put("CHINSES", rStr.get(i)[1]);
                                data.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectListView.setAdapter(new SelectBaseAdapter(activity_select.this, R.layout.select_item, data));
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override//搜索款点击事件
    public boolean onQueryTextSubmit(String query) {
        clear_out.setVisibility(View.GONE);
        if (cursor != null)
            cursor.close();
        cursor = null;
        String str = editText.getText().toString();
        boolean flag = urlQuerys(str);
        return flag;
    }


    @Override//搜索框文本改变事件
    public boolean onQueryTextChange(String newText) {
        List<Map<String, String>> data = getData();
        clear_out.setVisibility(View.GONE);
        if (data.size() == 0) {
            if (cursor != null)
                cursor.close();
            cursor = null;
            String str = editText.getText().toString();
            boolean flag = urlQuerys(str);
            return flag;
        }

        selectListView.setAdapter(new SelectBaseAdapter(this, R.layout.select_item, data));
        return true;
    }

    //模糊查询数据库中匹配的前十条记录，即前十个单词
    private List<Map<String, String>> getData() {//最多搜索十条记录
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map;

        String str = editText.getText().toString();

        if (str.equals(""))
            return data;
        cursor = new DatabaseUtil().select(str);
        if (cursor == null || cursor.getCount() == 0)
            return data;
        cursor.moveToFirst();
        for (int i = 0; i < 10; i++) {
            map = new HashMap<>();
            map.put("ENGLISH", cursor.getString(1));
            map.put("CHINSES", (cursor.getString(7) + cursor.getString(8) + "   " + cursor.getString(9)
                    + cursor.getString(10)).replace("null", ""));
            data.add(map);
            if (!cursor.moveToNext())
                break;
        }
        return data;
    }

}