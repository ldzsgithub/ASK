package com.jy23.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {
    private static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String Y_M_D(Date date) {
        return YMD.format(date);
    }
    public static String Y_M_D_H_M_S(Date date) {
        return YMDHMS.format(date);
    }

    public static Date Y_M_D(String date) {
        try {
            return YMD.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public static Date Y_M_D_H_M_S(String date) {
        try {
            return YMDHMS.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null ;
        }
    }


    public static String timeStamp() {
        return df.format(new Date());
    }


    public static void main(String[] args)   {
        L.e(TimeUtils.oneDayAllMinute("2018-08-21"));
    }

    public static List<String> oneDayAllMinute(String s)   {
        try {
            Calendar tt = Calendar.getInstance();
            tt.setTime(YMDHMS.parse(s+" 00:00:00"));
            List<String> list =new ArrayList<>(1440);
            for (int i = 0; i < 1440; i++) {
                list.add(YMDHMS.format(tt.getTime()));
                tt.add(Calendar.MINUTE,1);
            }
            return list;
        }catch (ParseException e){
            L.e("解析异常");
            return null;
        }
    }
}
