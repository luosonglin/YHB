package com.medmeeting.m.zhiyi.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String TYPE_01 = "yyyy-MM-dd HH:mm:ss";
    public static final String TYPE_02 = "yyyy-MM-dd";
    public static final String TYPE_03 = "HH:mm:ss";
    public static final String TYPE_04 = "yyyy年MM月dd日";
    public static final String TYPE_05 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String TYPE_06 = "yyyy-MM-dd HH:mm";
    public static final String TYPE_07 = "MM-dd";
    public static final String TYPE_08 = "MM-dd HH:mm";

    public DateUtil() {
    }

    public static String formatDate(long time, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return (new SimpleDateFormat(format)).format(cal.getTime());
    }

    public static String formatDate(String longStr, String format) {
        try {
            return formatDate(Long.parseLong(longStr), format);
        } catch (Exception var3) {
            return "";
        }
    }

    public static long formatStr(String timeStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            return sdf.parse(timeStr).getTime();
        } catch (ParseException var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }
}

