package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.MVP.Listener.SampleListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.animation.HeartLayout;
import com.medmeeting.m.zhiyi.UI.LiveView.live.fragment.BottomPanelFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.ChatListAdapter;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.message.GiftMessage;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.ChatListView;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;
import com.medmeeting.m.zhiyi.Util.DownloadImageTaskUtil;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.videoplayer.LandLayoutLivePlayer;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_NORMAL;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PREPAREING;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 10/11/2017 7:47 AM
 * @describe 全屏直播的播放器
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LivePlayerActivity2 extends AppCompatActivity implements Handler.Callback {

    //直播相关参数
    private Handler handler = new Handler(this);
    private ChatListView chatListView;
    private ChatListAdapter chatListAdapter;
    private BottomPanelFragment bottomPanel;
    private ImageView btnDan;
    private ImageView btnGift;
    private ImageView btnHeart;
    private HeartLayout heartLayout;
    private Random random = new Random();
    private TextView sumTv;

    private int programId;

    //播放器相关参数
    LandLayoutLivePlayer detailPlayer;
    private boolean isPlay;
    private boolean isPause;
    private OrientationUtils orientationUtils;
    private int countIncrement;
    private int countRatio;

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        programId = getIntent().getExtras().getInt("programId");
        initChat(programId);

        detailPlayer = (LandLayoutLivePlayer) findViewById(R.id.detail_player);

        countIncrement = getIntent().getIntExtra("countIncrement", 0);
        countRatio = getIntent().getIntExtra("countRatio", 1);
        initPlayer(getIntent().getStringExtra("url"), "","", countIncrement, countRatio);

    }

    @Override
    public void recreate() {
        initChat(programId);
        initPlayer(getIntent().getStringExtra("url"), "","", countIncrement, countRatio);
        super.recreate();
    }

    private void initChat(Integer programId) {
        //init 互动view
        LiveKit.addEventHandler(handler);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btnDan = (ImageView) bottomPanel.getView().findViewById(R.id.btn_dan);
        btnGift = (ImageView) bottomPanel.getView().findViewById(R.id.btn_gift);
        btnHeart = (ImageView) bottomPanel.getView().findViewById(R.id.btn_heart);
        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        btnDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatListView.getVisibility() == View.VISIBLE) {
                    chatListView.setVisibility(View.GONE);
                    btnDan.setImageResource(R.mipmap.icon_dan_close);
                } else if (chatListView.getVisibility() == View.GONE) {
                    chatListView.setVisibility(View.VISIBLE);
                    btnDan.setImageResource(R.mipmap.icon_dan);
                }
            }
        });
        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiftMessage msg = new GiftMessage("2", "送您一个礼物");
                LiveKit.sendMessage(msg);
            }
        });
        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        heartLayout.addHeart(rgb);
                    }
                });
                GiftMessage msg = new GiftMessage("1", "点赞了");
                LiveKit.sendMessage(msg);
            }
        });
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });


        joinChatRoom(programId + "");
    }

    /**
     * im
     */
    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom(roomId, 15, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了房间");
//                LiveKit.sendMessage(content);

                // 配合ios
                TextMessage content = TextMessage.obtain("进入了房间");
                LiveKit.sendMessage(content);

                Log.e(" joinChatRoom: ", content + "" + content.getUserInfo().getName());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Toast.makeText(LivePlayerActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e("", "聊天室加入失败! errorCode = " + errorCode);
            }
        });
    }

    /**
     * **********************以下为播放器**********
     */
    private void initPlayer(String url, String photo, String title, int countIncrement, int countRatio) {
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        if (url != null)
            gsyVideoOption//.setThumbImageView(imageView)
                    .setThumbPlay(false)    //是否点击封面可以播放
                    .setIsTouchWiget(true)  //是否可以滑动界面改变进度，声音等
                    .setRotateViewAuto(false)   //是否开启自动旋转
                    .setLockLand(false) //一全屏就锁屏横屏，默认false竖屏，可配合setRotateViewAuto使用
                    .setShowFullAnimation(false)    //是否使用全屏动画效果
                    .setNeedLockFull(true)  //是否需要全屏锁定屏幕功能
                    .setSeekRatio(1)    //调整触摸滑动快进的比例
                    .setUrl(url)
                    .setCacheWithPlay(false)
                    .setVideoTitle(title)
                    .setStandardVideoAllCallBack(new SampleListener() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            Debuger.printfError("***** onPrepared **** " + objects[0]);
                            Debuger.printfError("***** onPrepared **** " + objects[1]);
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
//                            initChat(url);
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            super.onAutoComplete(url, objects);
                        }

                        @Override
                        public void onClickStartError(String url, Object... objects) {
                            super.onClickStartError(url, objects);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                        }


                        @Override
                        public void onClickStartIcon(String url, Object... objects) {
                            super.onClickStartIcon(url, objects);
                        }
                    })
                    .setLockClickListener(new LockClickListener() {
                        @Override
                        public void onClick(View view, boolean lock) {
                            if (orientationUtils != null) {
                                //配合下方的onConfigurationChanged
                                orientationUtils.setEnable(!lock);
                            }
                        }
                    })
                    .build(detailPlayer);

        new DownloadImageTaskUtil(detailPlayer.getCoverPhoto()).execute(photo);
        if (detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING) {
            detailPlayer.getCoverPhoto().setVisibility(View.GONE);
        } else {
            detailPlayer.getCoverPhoto().setVisibility(View.VISIBLE);
        }


        if (detailPlayer.getFullscreenButton() != null) {
            detailPlayer.getFullscreenButton().setOnClickListener(v -> {

                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(LivePlayerActivity2.this, true, true);
            });
        }

        detailPlayer.getStartButton().setVisibility(View.VISIBLE);
        detailPlayer.getBuyButton().setVisibility(View.GONE);

        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(view -> finish());

        detailPlayer.getShareButton().setVisibility(View.GONE);
        detailPlayer.getShareButton().setOnClickListener(view -> ToastUtils.show(LivePlayerActivity2.this, "share"));

        detailPlayer.getFullscreenButton().setVisibility(View.GONE);


//
//        //直接横屏
//        orientationUtils.resolveByClick();
//
//        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
//        detailPlayer.startWindowFullscreen(LivePlayerActivity2.this, true, true);

        sumTv = (TextView) findViewById(R.id.sum);

        mHandler2 = new Handler();
        mHandler2.post(new Runnable() {
            @Override
            public void run() {
                LiveKit.getChatRoomSum(programId + "", 500, new RongIMClient.ResultCallback<ChatRoomInfo>() {
                    @Override
                    public void onSuccess(ChatRoomInfo chatRoomInfo) {
                        sumTv.setText("" + (countIncrement + chatRoomInfo.getTotalMemberCount() * countRatio));
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
                mHandler2.postDelayed(this, 2000);
            }
        });

        detailPlayer.getStartButton().performClick();
    }

    private Handler mHandler2;

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();

        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);

                // 配合ios，tony说"去掉离开了房间"
//                TextMessage content = TextMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);
//                Log.e(TAG, content + " " + content.getUserInfo().getName());

                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }
}
