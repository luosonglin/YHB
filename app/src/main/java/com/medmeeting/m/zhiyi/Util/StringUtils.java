package com.medmeeting.m.zhiyi.Util;

/**
 * Created by Luo Songlin on 5/25/16.
 * Feature: String工具类
 *
 */
public class StringUtils {
    public StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static String convertObjectToString(Object o) {
        return o != null?(o instanceof String? o.toString():(o instanceof Integer?"" + (Integer) o :(o instanceof Long?"" + (Long) o :(o instanceof Double?"" + (Double) o :(o instanceof Float?"" + (Float) o :(o instanceof Short?"" + (Short) o :(o instanceof Byte?"" + (Byte) o :(o instanceof Boolean? o.toString():(o instanceof Character? o.toString():o.toString()))))))))):"";
    }

    public static int hashCode(String value) {
        int h = 0;
        if(h == 0 && value.length() > 0) {
            char[] val = value.toCharArray();

            for (char aVal : val) {
                h = 31 * h + aVal;
            }
        }

        return h;
    }
}
