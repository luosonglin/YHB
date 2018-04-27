package com.medmeeting.m.zhiyi.UI.JS;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.medmeeting.m.zhiyi.Constant.Data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/12/2017 4:49 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class JSHook {
    @JavascriptInterface
    public void javaMethod(String p) {
        Log.e("javascript", "JSHook.JavaMethod() called! + " + p);
    }


    public String GETUID;

    @JavascriptInterface
    public String getUserIdInWeb(final String string) {
        try {
            // 解析js传递过来的json串
            JSONObject mJson = new JSONObject(string);
            GETUID = mJson.optString("GETUID");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("javascript", "getUserIdInWeb" + Data.getUserId() + " " + GETUID + " " + Data.getUserToken());
//            return userId;
        return Data.getUserToken();
    }

    @JavascriptInterface
    public void printWebLog(String str) {
        Log.e("javascript" + " WebView: ", str);
    }

    public String getInfo() {
        return "获取手机内的信息！！";
    }
}
