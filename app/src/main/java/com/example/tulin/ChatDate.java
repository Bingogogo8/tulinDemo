package com.example.tulin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatDate {

    /*格式化日期时间，用于显示时间   date -> String*/

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return simpleDateFormat.format(date);
    }


}
