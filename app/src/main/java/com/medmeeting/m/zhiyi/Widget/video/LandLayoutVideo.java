package com.medmeeting.m.zhiyi.Widget.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时候使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_video_land;
        }
        return R.layout.sample_video_normal;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

    /************************************* 业务需求 ****************************************/


    private ImageView mShareButton;

    /**
     * 获取分享按键
     */
    public ImageView getShareButton() {
        mShareButton = (ImageView) findViewById(R.id.share);
        return mShareButton;
    }


    private TextView mBuyButton;

    /**
     * 获取购买按键
     */
    public TextView getBuyButton() {
        mBuyButton = (TextView) findViewById(R.id.buy);
        return mBuyButton;
    }

    private ImageView mCoverPhoto;

    /**
     * 获取封面图片
     */
    public ImageView getCoverPhoto() {
        mCoverPhoto = (ImageView) findViewById(R.id.cover_photo);
        return mCoverPhoto;
    }

    /**
     * 获取当前的播放状态
     */
    public int getCurrentState() {
        return mCurrentState;
    }

}
