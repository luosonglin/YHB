package com.medmeeting.m.zhiyi.UI.LiveView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.animation.HeartLayout;
import com.medmeeting.m.zhiyi.UI.LiveView.live.fragment.BottomPanelFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.ChatListAdapter;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.message.GiftMessage;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.ChatListView;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;
import com.medmeeting.m.zhiyi.videoplayer.Utils;
import com.medmeeting.m.zhiyi.videoplayer.VideoPlayerBaseActivity;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.PLNetworkManager;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.net.UnknownHostException;
import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 观众播放器页
 */
public class LivePlayerActivity extends VideoPlayerBaseActivity implements Handler.Callback{

    private static final String TAG = LivePlayerActivity.class.getSimpleName();

    //以下为播放器参数
    private static final int MESSAGE_ID_RECONNECTING = 0x01;
    private static final String DEFAULT_TEST_URL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private static final String[] DEFAULT_PLAYBACK_DOMAIN_ARRAY = {
            "live.hkstv.hk.lxdns.com"
    };
    private PLVideoTextureView mVideoView;
    private Toast mToast = null;
    private String mVideoPath = null;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default
    private View mLoadingView;
    private boolean mIsActivityPaused = true;
    private int mIsLiveStreaming = 1;

    //以下为直播室互动参数
    private Handler handler = new Handler(this);
    private ChatListView chatListView;
    private ChatListAdapter chatListAdapter;
    private BottomPanelFragment bottomPanel;
    private ImageView btnDan;
    private ImageView btnGift;
    private ImageView btnHeart;
    private HeartLayout heartLayout;
    private Random random = new Random();

    private int programId;

    private boolean mIsEncOrientationPort = true;

    /**
     * 配置播放参数
     * @param codecType 解码方式
     */
    private void setOptions(int codecType) {
        AVOptions options = new AVOptions();

        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

        // 播放前最大探测流的字节数，单位是 byte
        // 默认值是：128 * 1024
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);

        // Some optimization with buffering mechanism when be set to 1
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming);
        if (mIsLiveStreaming == 1) {
            // 是否开启"延时优化"，只在在线直播流中有效
            options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }

        // 默认的缓存大小，单位是 ms
        // 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：4000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        options.setInteger(AVOptions.KEY_MEDIACODEC, codecType);

        // whether start play automatically after prepared, default value is 1
        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`, 0为非自动
        // 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        // 请在开始播放之前配置
        mVideoView.setAVOptions(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player);

        // 启动 DNS 缓存管理服务
        // 提前传入待解析的域名列表会在第一次播放时得到更好的体验，不提前传入也没有问题，每次播放前，DNS 缓存服务会自动缓存播放域名。
        try {
            PLNetworkManager.getInstance().startDnsCacheService(this, DEFAULT_PLAYBACK_DOMAIN_ARRAY);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mVideoView = (PLVideoTextureView) findViewById(R.id.VideoView);

        // 设置加载动画
        // 在播放器进入缓冲状态时，自动显示加载界面，缓冲结束后，自动隐藏加载界面
        mLoadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);


        //背景图
//        View mCoverView = (ImageView) findViewById(R.id.CoverView);
//        mVideoView.setCoverView(mCoverView);


        // If you want to fix display orientation such as landscape, you can use the code show as follow
        //
        // if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //     mVideoView.setPreviewOrientation(0);
        // }
        // else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        //     mVideoView.setPreviewOrientation(270);
        // }

        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
        mIsLiveStreaming = 1;//getIntent().getIntExtra("liveStreaming", 1);  //0是点播

        // 1 -> hw codec enable, 0 -> disable [recommended]
        // 硬解优先，失败后自动切换到软解
        int codec = AVOptions.MEDIA_CODEC_AUTO; // getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        setOptions(codec);

        // You can mirror the display
        // 画面的镜像变换，视频图像翻过来了
//         mVideoView.setMirror(true);

        // You can also use a custom `M7ediaController` widget
        //屏蔽此处，因为不需要播放器的操作事件
//        MediaController mMediaController = new MediaController(this, false, mIsLiveStreaming == 1);
//        mVideoView.setMediaController(mMediaController);

        //监控
//        mVideoView.setOnPreparedListener(mOnPreparedListener);
//        mVideoView.setOnInfoListener(mOnInfoListener);
//        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
//        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
//        mVideoView.setOnSeekCompleteListener(mOnSeekCompleteListener);
//        mVideoView.setOnCompletionListener(mOnCompletionListener);

//        // 设置显示播放封面
//        mLoadingView.setBackground(getResources().getDrawable(R.mipmap.xiaomai));
//        mVideoView.setCoverView(mLoadingView);

        // 设置播放地址
        mVideoPath = getIntent().getExtras().getString("rtmpPlayUrl"); //DEFAULT_TEST_URL;//getIntent().getStringExtra("videoPath");
        mVideoView.setVideoPath(mVideoPath);
//        mVideoView.setVideoPath(DEFAULT_TEST_URL);

        //默认为全屏横屏模式
        mRotation = (mRotation + 0) % 360;
        mVideoView.setDisplayOrientation(mRotation);

        mVideoView.start();

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

        programId = getIntent().getExtras().getInt("programId");
        Log.e(TAG, "programId" + programId);
        joinChatRoom(programId+"");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mToast = null;
        mVideoView.pause();
        mIsActivityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        // 停止 DNS 缓存管理服务
        PLNetworkManager.getInstance().stopDnsCacheService(this);


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

    public void onClickRotate(View v) {
//        mRotation = (mRotation + 90) % 360;
//        switch (mRotation) {
//            case 90:
//                mVideoView.setDisplayOrientation(90);
//                break;
//            case 180:
//                mVideoView.setDisplayOrientation(0);
//                break;
//            case 270:
//                mVideoView.setDisplayOrientation(90);
//                break;
//            case 0:
//                mVideoView.setDisplayOrientation(0);
//                break;
//        }

        mIsEncOrientationPort = !mIsEncOrientationPort;
        setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (mVideoView.getDisplayAspectRatio()) {
            case PLVideoTextureView.ASPECT_RATIO_ORIGIN:
                showToastTips("Origin mode");
                break;
            case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                showToastTips("Fit parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                showToastTips("Paved parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_16_9:
                showToastTips("16 : 9 !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_4_3:
                showToastTips("4 : 3 !");
                break;
            default:
                break;
        }
    }

    /**
     * 监听播放器的 prepare 过程，
     * 该过程主要包括：创建资源、建立连接、请求码流等等，
     * 当 prepare 完成后，SDK 会回调该对象的 onPrepared 接口，下一步则可以调用播放器的 start() 启动播放
     */
    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            showToastTips("onPrepared! " + plMediaPlayer.getDataSource());
        }
    };

    /**
     * 监听播放器的状态消息，
     * 在播放器启动后，SDK 会在播放器发生状态变化时调用该对象的 onInfo 方法，同步状态信息。
     */
    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_UNKNOWN:
                    showToastTips("未知消息");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    showToastTips("第一帧视频已成功渲染");
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    showToastTips("开始缓冲");
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    showToastTips("停止缓冲");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    showToastTips("获取到视频的播放角度");
                    return mVideoView.setDisplayOrientation(360 - extra);
//                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    showToastTips("第一帧音频已成功播放");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME:
                    showToastTips("获取视频的I帧间隔");
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    showToastTips("硬解失败，自动切换软解");
                    break;
                default:
                    showToastTips("unknown info!");
                    break;
            }
            return false;
        }
    };

    /**
     * 监听播放器的错误消息，
     * 一旦播放过程中产生任何错误信息，SDK 都会回调该接口，返回值决定了该错误是否已经被处理，
     * 如果返回 false，则代表没有被处理，下一步则会触发 onCompletion 消息
     */
    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    showToastTips("Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToastTips("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToastTips("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    setOptions(AVOptions.MEDIA_CODEC_SW_DECODE);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            if (isNeedReconnect) {
                // 需要注意，如果决定做重连，则 onError 回调中，请返回 true，否则会导致触发 onCompletion.
                sendReconnectMessage();
            } else {
                finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    /**
     * 监听播放结束的消息，关于该回调的时机，有如下定义：
     * 如果是播放文件，则是播放到文件结束后产生回调
     如果是在线视频，则会在读取到码流的EOF信息后产生回调，回调前会先播放完已缓冲的数据
     如果播放过程中产生onError，并且没有处理的话，最后也会回调本接口
     如果播放前设置了 setLooping(true)，则播放结束后会自动重新开始，不会回调本接口
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            showToastTips("Play Completed !");
            finish();
        }
    };

    /**
     * 监听当前播放器已经缓冲的数据量占整个视频时长的百分比，
     * 在播放直播流中无效，仅在播放文件和回放时才有效
     */
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int percent) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            showToastTips("当前播放器已经缓冲的数据量占整个视频时长的百分比：" + percent);
        }
    };

    /**
     * 监听当前播放的视频流的尺寸信息，
     * 在 SDK 解析出视频的尺寸信息后，会触发该回调，开发者可以在该回调中调整 UI 的视图尺寸
     */
    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1, int i2, int i3) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            showToastTips("当前播放的视频流的尺寸信息：" + i + i1 + i2 + i3 );
        }
    };

    /**
     * 监听 seek 完成的消息，
     * 当调用的播放器的 seekTo 方法后，SDK 会在 seek 成功后触发该回调
     */
    private PLMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new PLMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
            Log.e(TAG, " 播放器当前状态:"+plMediaPlayer.getPlayerState()
                    + "\n 当前播放流的 METADATA 信息:"+plMediaPlayer.getMetadata()
                    + "\n 实时统计信息:"+plMediaPlayer.getVideoFps() + plMediaPlayer.getVideoBitrate());
            showToastTips("监听 seek 完成的消息：" + plMediaPlayer.getDataSource());
        }
    };

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mToast != null) {
//                    mToast.cancel();
//                }
//                mToast = Toast.makeText(LivePlayerActivity.this, tips, Toast.LENGTH_SHORT);
//                mToast.show();
                Log.e(TAG, tips);
            }
        });
    }

    private Integer mReconnectTimes = 0;
    private void sendReconnectMessage() {
        if (mReconnectTimes >= 2) {
            finish();
            return;
        }
        showToastTips("正在重连...");
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                finish();
                return;
            }
            if (!Utils.isNetworkAvailable(LivePlayerActivity.this)) {
                // 重连之前，建议使用如下方法判断一下网络的联通性，并且在每次重连之前，delay 1～2s
                new AlertDialog.Builder(LivePlayerActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("主播正在赶来的路上，直播将在1s后进行重连")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mReconnectTimes++;
                                sendReconnectMessage();
                            }
                        })
                        .show();
                return;
            }
            mVideoView.setVideoPath(mVideoPath);
            mVideoView.start();
        }
    };

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
        LiveKit.joinChatRoom(roomId, 5, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了房间");
//                LiveKit.sendMessage(content);

                // 配合ios
                TextMessage content = TextMessage.obtain("进入了房间");
                LiveKit.sendMessage(content);

                Log.e(TAG+" joinChatRoom: ", content + "" + content.getUserInfo().getName());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Toast.makeText(LivePlayerActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "聊天室加入失败! errorCode = " + errorCode);
            }
        });
    }
}

