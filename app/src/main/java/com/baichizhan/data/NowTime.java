package com.baichizhan.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowTime {
    public NowTime(){

    }
    public String now(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
