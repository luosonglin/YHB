package com.medmeeting.m.zhiyi.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置自定义字体帮助类
 * Created by luosonglin on 16/11/3.
 */
public class FontHelper {

//    public static final String FONT = "fonts/Zhaimi-Medium.ttf";

    public static final String ICONFONT = "fonts/icomoon.ttf";

    private static final String TAG = "fontHelper";
    private static Map<String, Typeface> stringTypefaceMap = new HashMap<>();

    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView) {
                Typeface typeface = stringTypefaceMap.get(fontName);
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                    stringTypefaceMap.put(fontName, typeface);
                }

                ((TextView) root).setTypeface(typeface);
            }
        } catch (Exception e) {
            Log.e(TAG, String.format("Error occured when trying to apply %s font for %s view", fontName, root));
            e.printStackTrace();
        }
    }
}
