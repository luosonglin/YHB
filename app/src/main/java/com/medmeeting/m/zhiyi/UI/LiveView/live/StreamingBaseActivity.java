package com.medmeeting.m.zhiyi.UI.LiveView.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.fragment.BottomPanelFragment2;
import com.medmeeting.m.zhiyi.UI.LiveView.live.gles.FBO;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.ChatListAdapter;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.RotateLayout;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.ChatListView;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting.CAMERA_FACING_ID;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;
import com.qiniu.pili.droid.streaming.av.common.PLFourCC;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class StreamingBaseActivity extends Activity implements
        View.OnLayoutChangeListener,
        StreamStatusCallback,
        StreamingPreviewCallback,
        SurfaceTextureCallback,
        AudioSourceCallback,
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener,
        Handler.Callback {

    private static final String TAG = StreamingBaseActivity.class.getSimpleName();

    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms

    private static final int MSG_START_STREAMING = 0;
    private static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    private static final int MSG_MUTE = 3;
    private static final int MSG_FB = 4;
    private static final int MSG_PREVIEW_MIRROR = 5;
    private static final int MSG_ENCODING_MIRROR = 6;

    private Context mContext;

    protected Button mShutterButton;
    private Button mMuteButton;
    private Button mTorchBtn;
    private Button mCameraSwitchBtn;
    private Button mCaptureFrameBtn;
    private Button mEncodingOrientationSwitcherBtn;
    private Button mFaceBeautyBtn;
    private Button mDanBtn;
    private RotateLayout mRotateLayout;

    private Button mLogoutBtn;
    private Button mSumBtn;

    protected TextView mLogTextView;
    protected TextView mStatusTextView;
    protected TextView mStatView;

    protected boolean mShutterButtonPressed = false;
    private boolean mIsTorchOn = false;
    private boolean mIsNeedMute = false;
    private boolean mIsNeedFB = false;
    private boolean mIsEncOrientationPort = true;
    private boolean mIsPreviewMirror = false;
    private boolean mIsEncodingMirror = false;

    private String mStatusMsgContent;
    private String mLogContent = "\n";

    protected MediaStreamingManager mMediaStreamingManager;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected StreamingProfile mProfile;
    protected JSONObject mJSONObject;
    private boolean mOrientationChanged = false;

    protected boolean mIsReady = false;

    private int mCurrentZoom = 0;
    private int mMaxZoom = 0;

    private FBO mFBO = new FBO();

    private ScreenShooter mScreenShooter = new ScreenShooter();
    private Switcher mSwitcher = new Switcher();
    private EncodingOrientationSwitcher mEncodingOrientationSwitcher = new EncodingOrientationSwitcher();

    private int mCurrentCamFacingIndex;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_STREAMING:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // disable the shutter button before startStreaming
                            setShutterButtonEnabled(false);
                            boolean res = mMediaStreamingManager.startStreaming();
                            mShutterButtonPressed = true;
                            Log.i(TAG, "res:" + res);
                            if (!res) {
                                mShutterButtonPressed = false;
                                setShutterButtonEnabled(true);
                            }
                            setShutterButtonPressed(mShutterButtonPressed);
                        }
                    }).start();
                    break;
                case MSG_STOP_STREAMING:
                    if (mShutterButtonPressed) {
                        // disable the shutter button before stopStreaming
                        setShutterButtonEnabled(false);
                        boolean res = mMediaStreamingManager.stopStreaming();
                        if (!res) {
                            mShutterButtonPressed = true;
                            setShutterButtonEnabled(true);
                        }
                        setShutterButtonPressed(mShutterButtonPressed);
                    }
                    break;
                case MSG_SET_ZOOM:
                    mMediaStreamingManager.setZoomValue(mCurrentZoom);
                    break;
                case MSG_MUTE:
                    mIsNeedMute = !mIsNeedMute;
                    mMediaStreamingManager.mute(mIsNeedMute);
//                    updateMuteButtonText();
                    break;
                case MSG_FB:
                    mIsNeedFB = !mIsNeedFB;
                    mMediaStreamingManager.setVideoFilterType(mIsNeedFB ?
                            CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                            : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
//                    updateFBButtonText();
                    break;
                case MSG_PREVIEW_MIRROR:
                    mIsPreviewMirror = !mIsPreviewMirror;
                    mMediaStreamingManager.setPreviewMirror(mIsPreviewMirror);
                    Toast.makeText(mContext, "镜像成功", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ENCODING_MIRROR:
                    mIsEncodingMirror = !mIsEncodingMirror;
                    mMediaStreamingManager.setEncodingMirror(mIsEncodingMirror);
                    Toast.makeText(mContext, "镜像成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };

    /**
     * im
     */
    private Handler handler = new Handler(this);
    private ChatListView chatListView;
    private ChatListAdapter chatListAdapter;
    private BottomPanelFragment2 bottomPanel;
    private ImageView btnDan;
    private ImageView btnCameraSwitch;
    private ImageView btnTorch;
    private ImageView btnEncodingOrientationSwitcher;
    private ImageView btnCaptureFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mIsEncOrientationPort = true;
        } else if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mIsEncOrientationPort = false;
        }
        setRequestedOrientation(Config.SCREEN_ORIENTATION);

        setContentView(R.layout.activity_camera_streaming);

        String publishUrlFromServer = getIntent().getStringExtra(Config.EXTRA_KEY_PUB_URL);
        Integer programId = getIntent().getExtras().getInt("programId");
        Log.i(TAG, "publishUrlFromServer:" + publishUrlFromServer);

        mContext = this;

        mProfile = new StreamingProfile();

        if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_URL_PREFIX)) {
            // publish url
            try {
                mProfile.setPublishUrl(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_URL_PREFIX.length()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_JSON_PREFIX)) {
            try {
                mJSONObject = new JSONObject(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_JSON_PREFIX.length()));
                StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
                mProfile.setStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Invalid Publish Url", Toast.LENGTH_LONG).show();
        }

        StreamingProfile.AudioProfile aProfile = new StreamingProfile.AudioProfile(44100, 96 * 1024);
        StreamingProfile.VideoProfile vProfile = new StreamingProfile.VideoProfile(30, 1000 * 1024, 48);
        StreamingProfile.AVProfile avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
                .setEncodingSizeLevel(Config.ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY)
                .setDnsManager(getMyDnsManager())
                .setAdaptiveBitrateEnable(true)
                .setFpsControllerEnable(true)
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000))
                .setPreferredVideoEncodingSize(352,640);

        CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = cameraFacingId.ordinal();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setContinuousFocusModeEnabled(true)    //打开自动对焦功能
                .setRecordingHint(false)    //在部分机型开启 Recording Hint 之后，会出现画面卡帧等风险，所以请慎用该 API。如果需要实现高 fps 推流，可以考虑开启并加入白名单机制。
                .setCameraFacingId(cameraFacingId)
                .setResetTouchFocusDelayInMs(3000)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

        mIsNeedFB = true;
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

        initUIs(programId);

        //init 互动view
        LiveKit.addEventHandler(handler);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        bottomPanel = (BottomPanelFragment2) getFragmentManager().findFragmentById(R.id.bottom_bar);
        btnDan = (ImageView) bottomPanel.getView().findViewById(R.id.dan_btn);
        btnDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatListView.getVisibility() == View.VISIBLE) {
                    chatListView.setVisibility(View.GONE);
                    btnDan.setImageResource(R.mipmap.icon_dan_close);
                } else {
                    chatListView.setVisibility(View.VISIBLE);
                    btnDan.setImageResource(R.mipmap.icon_dan);
                }
            }
        });
        btnCameraSwitch = (ImageView) bottomPanel.getView().findViewById(R.id.camera_switch_btn);
        btnCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
            }
        });
        btnTorch = (ImageView) bottomPanel.getView().findViewById(R.id.torch_btn);
        btnTorch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsTorchOn) {
                            mIsTorchOn = true;
                            mMediaStreamingManager.turnLightOn();
                        } else {
                            mIsTorchOn = false;
                            mMediaStreamingManager.turnLightOff();
                        }
                        setTorchEnabled(mIsTorchOn);
                    }
                }).start();
            }
        });
        btnEncodingOrientationSwitcher = (ImageView) bottomPanel.getView().findViewById(R.id.orientation_btn);
        btnEncodingOrientationSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mEncodingOrientationSwitcher);
                mHandler.post(mEncodingOrientationSwitcher);
            }
        });
        btnCaptureFrame = (ImageView) bottomPanel.getView().findViewById(R.id.capture_btn);
        btnCaptureFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mScreenShooter);
                mHandler.postDelayed(mScreenShooter, 100);
            }
        });
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });

        Log.e(TAG, "programId" + programId);
        joinChatRoom(programId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsReady = false;
        mShutterButtonPressed = false;
        mHandler.removeCallbacksAndMessages(null);
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();

//        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
//            @Override
//            public void onSuccess() {
//                // 配合ios
////                TextMessage content = TextMessage.obtain("离开了房间");
////                LiveKit.sendMessage(content);
////                Log.e(TAG, content + " " + content.getUserInfo().getName());
//
//                LiveKit.removeEventHandler(handler);
//                LiveKit.logout();
////                Toast.makeText(StreamingBaseActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//                LiveKit.removeEventHandler(handler);
//                LiveKit.logout();
////                Toast.makeText(StreamingBaseActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButtonPressed = pressed;
                mShutterButton.setPressed(pressed);
            }
        });
    }

    protected void setShutterButtonEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButton.setFocusable(enable);
                mShutterButton.setClickable(enable);
                mShutterButton.setEnabled(enable);
            }
        });
    }

    protected void startStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_START_STREAMING), 50);
    }

    protected void stopStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_STOP_STREAMING), 50);
    }

    @Override
    public boolean onRecordAudioFailedHandled(int err) {
        mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
        mMediaStreamingManager.startStreaming();
        return true;
    }

    @Override
    public boolean onRestartStreamingHandled(int err) {
        Log.i(TAG, "onRestartStreamingHandled");
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        Camera.Size size = null;
        if (list != null) {
            for (Camera.Size s : list) {
                if (s.height >= 480) {
                    size = s;
                    break;
                }
            }
        }
//        Log.e(TAG, "selected size :" + size.width + "x" + size.height);
        return size;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());

        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);

            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:" + mMaxZoom);
            if (!mHandler.hasMessages(MSG_SET_ZOOM)) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ZOOM), ZOOM_MINIMUM_WAIT_MILLIS);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.i(TAG, "view!!!!:" + v);
    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int width, int height, int rotation, int fmt, long tsInNanoTime) {
        Log.i(TAG, "onPreviewFrame " + width + "x" + height + ",fmt:" + (fmt == PLFourCC.FOURCC_I420 ? "I420" : "NV21") + ",ts:" + tsInNanoTime + ",rotation:" + rotation);
//        deal with the yuv data.
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = 0x00;
//        }
//        Log.i(TAG, "old onPreviewFrame cost :" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public void onSurfaceCreated() {
        Log.i(TAG, "onSurfaceCreated");
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        // newTexId should not equal with texId. texId is from the SurfaceTexture.
        // Otherwise, there is no filter effect.
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
//        Log.i(TAG, "onDrawFrame texId:" + texId + ",newTexId:" + newTexId + ",texWidth:" + texWidth + ",texHeight:" + texHeight);
        return newTexId;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int size, long tsInNanoTime, boolean eof) {
//        for (int i = 0; i < size; i++) {
//            byteBuffer.put(i, (byte) 0x00);
//        }
    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatView.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
                        + "\naudio:" + streamStatus.audioFps + " fps"
                        + "\nvideo:" + streamStatus.videoFps + " fps");
            }
        });
    }

    private void setTorchEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                String flashlight = enabled ? getString(R.string.flash_light_off) : getString(R.string.flash_light_on);
//                mTorchBtn.setText("");
//                mTorchBtn.setBackgroundResource(enabled ? R.mipmap.icon_close_light : R.mipmap.icon_open_light);
                btnTorch.setImageResource(enabled ? R.mipmap.icon_close_light : R.mipmap.icon_open_light);
            }
        });
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        Log.i(TAG, "StreamingState streamingState:" + streamingState + ",extra:" + extra);

        switch (streamingState) {
            case PREPARING:
                mStatusMsgContent = getString(R.string.string_state_preparing);
                break;
            case READY:
                mIsReady = true;
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
                mStatusMsgContent = getString(R.string.string_state_ready);
                // start streaming when READY
                startStreaming();
                break;
            case CONNECTING:
                mStatusMsgContent = getString(R.string.string_state_connecting);
                break;
            case STREAMING:
                mStatusMsgContent = getString(R.string.string_state_streaming);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(true);
                break;
            case SHUTDOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(false);
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
                break;
            case IOERROR:
                mLogContent += "IOERROR\n";
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                break;
            case UNKNOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case SENDING_BUFFER_EMPTY:
                break;
            case SENDING_BUFFER_FULL:
                break;
            case AUDIO_RECORDING_FAIL:
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case DISCONNECTED:
                mLogContent += "DISCONNECTED\n";
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
            case CAMERA_SWITCHED:
//                mShutterButtonPressed = false;
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isSupportedTorch) {
//                                mTorchBtn.setVisibility(View.VISIBLE);
                                btnTorch.setVisibility(View.VISIBLE);
                            } else {
//                                mTorchBtn.setVisibility(View.GONE);
                                btnTorch.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogTextView != null) {
                    mLogTextView.setText(mLogContent);
                }
                mStatusTextView.setText(mStatusMsgContent);
            }
        });
    }

    private void initUIs(final Integer programId) {
        View rootView = findViewById(R.id.content);
        rootView.addOnLayoutChangeListener(this);

//        mMuteButton = (Button) findViewById(R.id.mute_btn);
        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
//        mTorchBtn = (Button) findViewById(R.id.torch_btn);
//        mCameraSwitchBtn = (Button) findViewById(R.id.camera_switch_btn);
//        mCaptureFrameBtn = (Button) findViewById(R.id.capture_btn);
//        mFaceBeautyBtn = (Button) findViewById(R.id.fb_btn);
//        mDanBtn = (Button) findViewById(R.id.dan_btn);
        mStatusTextView = (TextView) findViewById(R.id.streamingStatus);
        Button previewMirrorBtn = (Button) findViewById(R.id.preview_mirror_btn);
        Button encodingMirrorBtn = (Button) findViewById(R.id.encoding_mirror_btn);

        mLogTextView = (TextView) findViewById(R.id.log_info);
        mStatView = (TextView) findViewById(R.id.stream_status);

        mLogoutBtn = (Button) findViewById(R.id.logout_btn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(StreamingBaseActivity.this)
                        .setIcon(R.mipmap.logo)
                        .setTitle("")
                        .setMessage("确定退出直播？")
                        .setNegativeButton("确定", (dialogInterface, i) -> finish())
//                        .setPositiveButton("彻底关闭", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                HttpData.getInstance().HttpDataCloseProgram(new Observer<HttpResult3>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        Log.e(TAG, "onCompleted");
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Log.e(TAG, "onError: " + e.getMessage()
//                                                + "\n" + e.getCause()
//                                                + "\n" + e.getLocalizedMessage()
//                                                + "\n" + e.getStackTrace());
//                                    }
//
//                                    @Override
//                                    public void onNext(HttpResult3 httpResult3) {
//                                        Log.e(TAG, "onNext");
//                                        if (httpResult3.getStatus().equals("success"))
//                                            finish();
//                                    }
//                                }, programId);
//                            }
//                        })
                        .setNeutralButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }
        });


//        mFaceBeautyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mHandler.hasMessages(MSG_FB)) {
//                    mHandler.sendEmptyMessage(MSG_FB);
//                }
//            }
//        });

//        mDanBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (chatListView.getVisibility() == View.VISIBLE){
//                    chatListView.setVisibility(View.GONE);
//                    mDanBtn.setBackgroundResource(R.mipmap.icon_dan_close);
//                } else {
//                    chatListView.setVisibility(View.VISIBLE);
//                    mDanBtn.setBackgroundResource(R.mipmap.icon_dan);
//                }
//            }
//        });

//        mMuteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mHandler.hasMessages(MSG_MUTE)) {
//                    mHandler.sendEmptyMessage(MSG_MUTE);
//                }
//            }
//        });

        previewMirrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_PREVIEW_MIRROR)) {
                    mHandler.sendEmptyMessage(MSG_PREVIEW_MIRROR);
                }
            }
        });

        encodingMirrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_ENCODING_MIRROR)) {
                    mHandler.sendEmptyMessage(MSG_ENCODING_MIRROR);
                }
            }
        });

        //底部圆形按钮
        mShutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShutterButtonPressed) {
                    stopStreaming();
                } else {
                    startStreaming();
                }
            }
        });

//        mTorchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!mIsTorchOn) {
//                            mIsTorchOn = true;
//                            mMediaStreamingManager.turnLightOn();
//                        } else {
//                            mIsTorchOn = false;
//                            mMediaStreamingManager.turnLightOff();
//                        }
//                        setTorchEnabled(mIsTorchOn);
//                    }
//                }).start();
//            }
//        });

//        mCameraSwitchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mHandler.removeCallbacks(mSwitcher);
//                mHandler.postDelayed(mSwitcher, 100);
//            }
//        });

//        mCaptureFrameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mHandler.removeCallbacks(mScreenShooter);
//                mHandler.postDelayed(mScreenShooter, 100);
//            }
//        });


//        mEncodingOrientationSwitcherBtn = (Button) findViewById(R.id.orientation_btn);
//        mEncodingOrientationSwitcherBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.removeCallbacks(mEncodingOrientationSwitcher);
//                mHandler.post(mEncodingOrientationSwitcher);
//            }
//        });

        SeekBar seekBarBeauty = (SeekBar) findViewById(R.id.beautyLevel_seekBar);
        seekBarBeauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                fbSetting.beautyLevel = progress / 100.0f;
                fbSetting.whiten = progress / 100.0f;
                fbSetting.redden = progress / 100.0f;

                mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        initButtonText();
    }

    private void initButtonText() {
//        updateFBButtonText();
        updateCameraSwitcherButtonText(mCameraStreamingSetting.getReqCameraId());
//        mCaptureFrameBtn.setText("Capture");
//        updateFBButtonText();
//        updateMuteButtonText();
//        updateOrientationBtnText();
    }

    private void updateOrientationBtnText() {
//        if (mIsEncOrientationPort) {
//            mEncodingOrientationSwitcherBtn.setText("Land");
//        } else {
//            mEncodingOrientationSwitcherBtn.setText("Port");
//        }
    }

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }

//    private void updateFBButtonText() {
//        if (mFaceBeautyBtn != null) {
//            mFaceBeautyBtn.setText(mIsNeedFB ? "FB Off" : "FB On");
//        }
//    }

//    private void updateMuteButtonText() {
//        if (mMuteButton != null) {
//            mMuteButton.setText(mIsNeedMute ? "Unmute" : "Mute");
//        }
//    }

    private void updateCameraSwitcherButtonText(int camId) {
        if (mCameraSwitchBtn == null) {
            return;
        }
//        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mCameraSwitchBtn.setText("Back");
//        } else {
//            mCameraSwitchBtn.setText("Front");
//        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "截图保存在: " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    private CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();

            CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
    }

    private class EncodingOrientationSwitcher implements Runnable {

        @Override
        public void run() {
            Log.i(TAG, "mIsEncOrientationPort:" + mIsEncOrientationPort);
            stopStreaming();
            mOrientationChanged = !mOrientationChanged;
            mIsEncOrientationPort = !mIsEncOrientationPort;
            mProfile.setEncodingOrientation(mIsEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
            mMediaStreamingManager.setStreamingProfile(mProfile);
            setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaStreamingManager.notifyActivityOrientationChanged();
//            updateOrientationBtnText();
//            Toast.makeText(StreamingBaseActivity.this, Config.HINT_ENCODING_ORIENTATION_CHANGED, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "EncodingOrientationSwitcher -");
        }
    }

    private class ScreenShooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
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

    private void joinChatRoom(final Integer roomId) {

        LiveKit.joinChatRoom(roomId + "", 50, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了房间");
//                LiveKit.sendMessage(content);

                // 配合ios
                TextMessage content = TextMessage.obtain("进入了房间，房间号："+roomId);
                LiveKit.sendMessage(content);

//                Log.e(TAG + "joinChatRoom: ", content + "" + content.getUserInfo().getName());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Toast.makeText(StreamingBaseActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "聊天室加入失败! errorCode = " + errorCode);
            }
        });

        mSumBtn = (Button) findViewById(R.id.people_sum_btn);

//        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 5*1000, );

        mHandler2 = new Handler();
        mHandler2.post(new Runnable() {
            @Override
            public void run() {
                LiveKit.getChatRoomSum(roomId + "", 500, new RongIMClient.ResultCallback<ChatRoomInfo>() {
                    @Override
                    public void onSuccess(ChatRoomInfo chatRoomInfo) {
                        mSumBtn.setText("" + chatRoomInfo.getTotalMemberCount());
                        Log.e(TAG + "eee", chatRoomInfo.getTotalMemberCount() + "");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e(TAG + "eee", errorCode.getMessage());
                    }
                });
//                sum++;
//                mSumBtn.setText("" + sum);
                mHandler2.postDelayed(this, 2000);
            }
        });
    }

//    AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
    private Handler mHandler2;

    int sum = 0;
}
