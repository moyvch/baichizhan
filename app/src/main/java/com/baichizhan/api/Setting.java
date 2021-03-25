package com.baichizhan.api;
/*
* 设置类
* */
public class Setting {
    //查词接口
    public static String[] T_API={
            "https://fanyi.youdao.com/translate?doctype=json&i=",//有道，自动英汉互译
            "https://api.muxiaoguo.cn/api/Tn_google?se=en&word=",//google,需指定语种
            //网络单词联想
            "https://dict.iciba.com/dictionary/word/suggestion?nums=5&ck=709a0db45332167b0e2ce1868b84773e&timestamp=1609215779630&client=6&uid=123123&key=1000006&is_need_mean=1&signature=fc44f4f641a4268151eca0a783ba82b7&word=",
            "http://dict-co.iciba.com/api/dictionary.php?key=1F9CA812CB18FFDFC95FC17E9C57A5E1&w="
    };
    //发音接口，调用时使用字符串的字符替换函数将*替换
    public static String[] V_API={
            "https://dict.youdao.com/dictvoice?type=0&audio=*",//有道英语发音，type=0为美国发音，type=1为英国发音
            "https://media.shanbay.com/audio/us/*.mp3",//扇贝发音，us为美音，uk为英音
            "https://api.microsofttranslatsor.com//V2//http.svc//Speak?appId=T6dIUxrCZ-oSjBZO_SRyMpo7ngY3fBRMHrRJc4KM_h6ZjzIZdc6xmXUGg_NAl94Jc" +
                    "&text=*&language=zh-CN&format=audio%2fmp3"//中文发音
    };
    //查词接口选择
    public static int T_INDEX=0;
    //发音接口选择
    public static int V_INDEX=0;
}
