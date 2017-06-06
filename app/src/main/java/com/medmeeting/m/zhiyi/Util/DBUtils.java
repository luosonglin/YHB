package com.medmeeting.m.zhiyi.Util;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * —————————————————————–
 * Copyright (C) 2016，上海知醫有限公司， All rights reserved.
 * —————————————————————–
 * Created by luosonglin on 20/11/2016.
 * —————————————————————–
 * (Feature) 数据库的工具类
 * —————————————————————–
 */

@SuppressWarnings("JavaDoc")
public class DBUtils {

    public static final String AUTHOR = "luosonglin";
    public static final String SELFCART = "jd";
    public static final String ZMCART = "zm";

    /**
     * 保存购物车数据到数据库 存在两个数据库jd 和 zm
     *
     * @param context 上下文
     * @param key     键值,分为两种 zm 和 jd
     * @param value   Cart的Json字符串
     * @return 正常执行的返回值依然是false
     * @throws SnappydbException
     */
    public static boolean put(Context context, String key, String value) throws SnappydbException {
        DB db = DBFactory.open(context);
        db.put(key, value);
        db.close();
        return false;
    }

    /**
     * 返回购物车里面的数据
     *
     * @param context 应用上下文
     * @param key     键值信息 jd 或者 zm
     * @return 购物车的Json字符串
     * @throws SnappydbException
     */
    public static String get(Context context, String key) throws SnappydbException {
        DB db = DBFactory.open(context);
        String value = db.get(key);
        db.close();
        return value;
    }

    /**
     * 检查是否存在要查询的数据库
     *
     * @param context 上下文信息
     * @param key     jd 或者 zm
     * @return true代表存在 false代表不存在
     * @throws SnappydbException
     */
    public static boolean isSet(Context context, String key) throws SnappydbException {
        DB db = DBFactory.open(context);
        boolean isSet = db.exists(key);
        db.close();
        return isSet;
    }

    /**
     * 删除数据
     *
     * @param context 上下文
     * @param key     数据库名字 jd 或者 zm
     * @throws SnappydbException
     */
    public static void del(Context context, String key) throws SnappydbException {
        DB db = DBFactory.open(context);
        if (db.exists(key)) {
            db.del(key);
        }
        db.close();
    }
}
