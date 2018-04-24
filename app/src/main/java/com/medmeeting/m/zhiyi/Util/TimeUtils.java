package com.medmeeting.m.zhiyi.Util;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 28/11/2017 1:35 PM
 * @describe TODO   时间工具类
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class TimeUtils {
    public TimeUtils() {
    }

    public static String getTimeFromSecond(int second) {
        if (second > 3600) {
            if (second / 3600 < 10) {
                if ((second % 3600) / 60 < 10) {
                    if ((second % 3600) % 60 < 10) {
                        return "0" + second / 3600 + ":0" + (second % 3600) / 60 + ":0" + (second % 3600) % 60;
                    } else {
                        return "0" + second / 3600 + ":0" + (second % 3600) / 60 + ":" + (second % 3600) % 60;
                    }
                } else {
                    if ((second % 3600) % 60 < 10) {
                        return "0" + second / 3600 + ":" + (second % 3600) / 60 + ":0" + (second % 3600) % 60;
                    } else {
                        return "0" + second / 3600 + ":" + (second % 3600) / 60 + ":" + (second % 3600) % 60;
                    }
                }
            } else {
                if ((second % 3600) / 60 < 10) {
                    if ((second % 3600) % 60 < 10) {
                        return second / 3600 + ":0" + (second % 3600) / 60 + ":0" + (second % 3600) % 60;
                    } else {
                        return second / 3600 + ":0" + (second % 3600) / 60 + ":" + (second % 3600) % 60;
                    }
                } else {
                    if ((second % 3600) % 60 < 10) {
                        return second / 3600 + ":" + (second % 3600) / 60 + ":0" + (second % 3600) % 60;
                    } else {
                        return second / 3600 + ":" + (second % 3600) / 60 + ":" + (second % 3600) % 60;
                    }
                }
            }

        } else {
            if (second / 60 < 10) {
                if (second % 60 < 10) {
                    return "0" + second / 60 + ":0" + second % 60;
                } else {
                    return "0" + second / 60 + ":" + second % 60;
                }
            } else {
                if (second % 60 < 10) {
                    return second / 60 + ":0" + second % 60;
                } else {
                    return second / 60 + ":" + second % 60;
                }
            }
        }
    }

}
