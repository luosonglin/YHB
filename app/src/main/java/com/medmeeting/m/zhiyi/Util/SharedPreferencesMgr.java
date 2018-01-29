package com.medmeeting.m.zhiyi.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * SharedPreferences管理类
 */
public class SharedPreferencesMgr {

    private static Context context;
    private static SharedPreferences sPrefs;

    private SharedPreferencesMgr(Context context, String fileName) {
        SharedPreferencesMgr.context = context;
        sPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        //7.0后
//        代码调用，存放在data/data/包名/xxx
//        目录下的文件设置
//        Context.MODE_WORLD_READABLE = 1
//        Context.MODE_WORLD_WRITEABLE = 2
//        会抛安全异常！！！
    }

    public static void init(Context context, String fileName) {
        new SharedPreferencesMgr(context, fileName);
    }

    public static String fileName;

    public static int getInt(String key, int defaultValue) {
        return sPrefs.getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        sPrefs.edit().putInt(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sPrefs.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        sPrefs.edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        if (sPrefs == null)
            return defaultValue;
        return sPrefs.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        if (sPrefs == null)
            return;
        sPrefs.edit().putString(key, value).commit();
    }

    //保存集合类
    public static void setList(String key, Set<String> value) {
        if (sPrefs == null)
            return;
        sPrefs.edit().putStringSet(key, value).commit();
    }
    public static Set<String> getList(String key, Set<String> defailtValue) {
        if (sPrefs == null)
            return defailtValue;
        return sPrefs.getStringSet(key, defailtValue);
    }

    public static void clearAll() {
        if (sPrefs == null)
            return;
        sPrefs.edit().clear().commit();
    }
}
