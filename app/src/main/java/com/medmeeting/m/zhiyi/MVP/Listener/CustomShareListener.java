package com.medmeeting.m.zhiyi.MVP.Listener;

import android.app.Activity;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;


/**
 * 分享listener
 */
public class CustomShareListener implements UMShareListener {

    private WeakReference<Activity> mActivity;

    public CustomShareListener(Activity activity) {
        mActivity = new WeakReference(activity);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA platform) {

        if (platform.name().equals("WEIXIN_FAVORITE")) {
            Toast.makeText(mActivity.get(), "收藏成功", Toast.LENGTH_SHORT).show();
        } else {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(mActivity.get(), "分享成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
        if (platform != SHARE_MEDIA.MORE) {
            Toast.makeText(mActivity.get(), "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.e(mActivity.getClass().getSimpleName(), "umeng throw:" + t.getMessage());
            }
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
        Toast.makeText(mActivity.get(), "分享取消", Toast.LENGTH_SHORT).show();
    }
}