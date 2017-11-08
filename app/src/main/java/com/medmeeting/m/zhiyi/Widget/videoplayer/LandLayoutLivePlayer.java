package com.medmeeting.m.zhiyi.Widget.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 08/11/2017 4:26 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LandLayoutLivePlayer extends StandardGSYVideoPlayer {

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutLivePlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutLivePlayer(Context context) {
        super(context);
    }

    public LandLayoutLivePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_live_land;
        }
        return R.layout.sample_live_normal;
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
