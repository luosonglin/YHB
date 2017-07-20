package com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller;


import com.medmeeting.m.zhiyi.BaseApplication;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
