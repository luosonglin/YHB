package com.medmeeting.m.zhiyi.Util;

import java.util.regex.Pattern;

/**
 * —————————————————————–—————————————————————–
 * Copyright (C) 2017，Luo Songlin，All rights reserved.
 * Created by luosonglin on 10/03/2017.
 * (Feature)
 * —————————————————————–—————————————————————–
 */

public class PhoneUtils {
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$"; //验证手机号
    public static final String REGEX_PASSWORD = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9]{6,15}"; //验证密码,密码至少8位，包含数字，大写字母及小写字母

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验密码
     * 密码至少8位，包含数字，大写字母及小写字母
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return password.length() >= 8 && Pattern.matches(REGEX_PASSWORD, password);
    }

    public static void main(String[] args) {
        System.out.print(isMobile("18239680724"));
    }
}
