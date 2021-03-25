package com.baichizhan.api;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.baichizhan.api.Setting.*;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

/*
* 网络api请求及处理
* */
public class API_Util {

    public static MediaPlayer mediaPlayer;

    //传入url朗读
    public static void start_V(String url) {
        if (!url.contains("https"))
            url = url.replace("http", "https");
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //传入单词或句子朗读
    public static void v_api(String query) {
        String url;
        if (query == null || query.equals(""))
            return;
        if (query.length() == query.getBytes().length) {//true为英
            url = V_API[V_INDEX];
        } else {
            url = V_API[2];
        }
        url = url.replace("*", query);
        start_V(url);
    }

    //翻译  返回翻译结果
    public static String t_api(String query) throws Exception {
        String url = T_API[T_INDEX];
        //中英文判断
        if (T_INDEX == 1 && query.length() == query.getBytes().length) {//true为英
            url.replace("en", "zh");
        }

        String resultStr = getJSON(url + query);

        //解析
        resultStr = parseJSONWithJSONObject(resultStr);

        if (resultStr == null)
            return null;
        return resultStr;
    }

    //传入单词联想查询（金山）
    public static List<String[]> l_api(String query) {
        String url = T_API[2];
        String data = getJSON(url + query);
        List<String[]> listData = LJSON(data);
        return listData;
    }

    /*
    * json获取
    * 传入url获取返回解析数据
    * */
    private static String getJSON(String url) {
        String line, resultStr = "";
        try {
            URL restURL = new URL(url);
            /*
             * 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection
             */
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            //请求方式
            conn.setRequestMethod("GET");
            //等待
            conn.setConnectTimeout(2000);
            //设置是否从httpUrlConnection读入，默认情况下是false; httpUrlConnection.setDoInput(true);
            //conn.setDoOutput(false);//设置为true将返回POST获取中的json字串，false则否
            //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
            conn.setAllowUserInteraction(false);

            BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (null != (line = bReader.readLine())) {
                resultStr += line;
            }
            bReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /*
    * 金山xml获取和解析
    * 传入单词返回单词数据
    * */
    public static List<ArrayList<String>> loadxml(String query) {
        List<ArrayList<String>> list = new ArrayList();
        ArrayList<String> data1 = new ArrayList<>();//单词
        ArrayList<String> data2 = new ArrayList<>();//英标，发音地址
        ArrayList<String> data3 = new ArrayList<>();//释义
        ArrayList<String> data4 = new ArrayList<>();//例句
        InputStream inputStream;
        try {
            //获得输入流
            URL rURL = new URL(T_API[3] + query);
            HttpURLConnection conn = (HttpURLConnection) rURL.openConnection();
            conn.setConnectTimeout(1000);
            inputStream = conn.getInputStream();

            //测试获取数据效果，以字符串形式输出
//            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line, resultStr = "";
//
//            while (null != (line = bReader.readLine())) {
//                resultStr += line;
//            }
//            System.out.println("我的input流---" + resultStr);

            //对返回的xml解析，分类存到list中
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "dict");

            int eve = parser.getEventType();
            while (eve != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();

                if (eve == START_TAG) {
                    if (nodeName.equals("key")) {
                        data1.add(parser.nextText());
                    } else if (nodeName.equals("ps")) {
                        data2.add(parser.nextText());
                    } else if (nodeName.equals("pron")) {
                        data2.add(parser.nextText());
                    } else if (nodeName.equals("pos")) {
                        data3.add(parser.nextText());
                    } else if (nodeName.equals("acceptation")) {
                        data3.add(parser.nextText().trim());
                    } else if (nodeName.equals("orig")) {
                        data4.add(parser.nextText().trim());
                    } else if (nodeName.equals("trans")) {
                        data4.add(parser.nextText().trim());
                    }
                }

                eve = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        return list;
    }

    //单词联想json解析
    private static List<String[]> LJSON(String jsonData) {
        List<String[]> list = new ArrayList<>();
        String[] arrData;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray data = jsonObject.getJSONArray("message");
            for (int i = 0; i < data.length(); i++) {
                arrData = new String[2];
                JSONObject jo = data.getJSONObject(i);
                if (jo.getString("key").length() == jo.getString("key").getBytes().length) {//判断是中文还是英文
                    arrData[0] = jo.getString("key");
                    arrData[1] = jo.getString("paraphrase");
                } else {
                    arrData[1] = jo.getString("key");
                    arrData[0] = jo.getString("paraphrase");
                }
                list.add(arrData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /*
    * 解析有道和google翻译的json，返回翻译结果
    * 暂时弃用！！！
    * */
    private static String parseJSONWithJSONObject(String jsonData) {
        String dataStr = new String();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            if (T_INDEX == 0) {//有道
                if (jsonObject.getString("errorCode").equals("0")) {//判断是否请求成功
                    JSONObject data = new JSONObject(jsonObject.getString("translateResult").replace("[", "").replace("]", ""));
//                    arrData[0] = data.getString("src");
                    dataStr = data.getString("tgt");
                } else {
                    return null;
                }

            } else if (T_INDEX == 1) {//google
                if (jsonObject.getString("code").equals("200")) {//判断是否请求成功
                    JSONObject data = jsonObject.getJSONObject("data");
//                    arrData[0] = data.getString("Original");
                    dataStr = data.getString("Translation");
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataStr;
    }


    //判断有无网络
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
