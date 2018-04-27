package com.medmeeting.m.zhiyi.Widget.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.animation.HeartLayout;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.ChatListView;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;
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
//            return R.layout.sample_video_land;
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

    /************************************* 直播业务需求 ****************************************/


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


    /************************************* 聊天业务需求 ****************************************/

    //以下为直播室互动参数
    private ChatListView chatListView;
    private ImageView btnDan;
    private ImageView btnGift;
    private ImageView btnHeart;
    private HeartLayout heartLayout;

    private ViewGroup buttonPanel;
    private ImageView btnInput;
    private InputPanel inputPanel;


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 获取对话列表
     */
    public ChatListView getChatListView() {
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        return chatListView;
    }

    /**
     * 获取对话列表
     */
    public ViewGroup getButtonPanel() {
        buttonPanel = (ViewGroup) findViewById(R.id.button_panel);
        return buttonPanel;
    }

    public ImageView getBtnInput() {
        btnInput = (ImageView) findViewById(R.id.btn_input);
        return btnInput;
    }

    public InputPanel getInputPanel() {
        inputPanel = (InputPanel) findViewById(R.id.input_panel);
        return inputPanel;
    }

    public void setInputPanelListener(InputPanel.InputPanelListener l) {
        inputPanel.setPanelListener(l);
    }


    /**
     * 获取弹幕按钮
     */
    public ImageView getBtnDan() {
        btnDan = (ImageView) findViewById(R.id.btn_dan);
        return btnDan;
    }

    /**
     * 获取礼物按钮
     */
    public ImageView getBtnGift() {
        btnGift = (ImageView) findViewById(R.id.btn_gift);
        return btnGift;
    }

    /**
     * 获取礼物按钮
     */
    public ImageView getBtnHeart() {
        btnHeart = (ImageView) findViewById(R.id.btn_heart);
        return btnHeart;
    }

    /**
     * 获取爱心区域
     */
    public HeartLayout getHeartLayout() {
        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        return heartLayout;
    }

}
