package com.medmeeting.m.zhiyi.UI.LiveView.live;

import android.os.Bundle;

import com.medmeeting.m.zhiyi.R;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

/**
 * Created by jerikc on 15/10/29.
 */
public class SWCodecCameraStreamingActivity extends StreamingBaseActivity {
    private static final String TAG = "SWCodecCameraStreaming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);

        // Decide FULL screen or real size
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);
        CameraPreviewFrameView cameraPreviewFrameView =
                (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(this);

        //logo水印
        WatermarkSetting watermarksetting = new WatermarkSetting(this);
        watermarksetting.setResourceId(R.mipmap.transparent)
                .setAlpha(100)
                .setSize(WatermarkSetting.WATERMARK_SIZE.SMALL)
                .setCustomPosition(0.5f, 0.5f)
                //如果根据此默认设置，水印在播放端看着非常模糊的话，可以尝试加入如下设置
                .setInJustDecodeBoundsEnabled(false);

        mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
                AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC); // sw codec

//        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, watermarksetting, mProfile, new PreviewAppearance(0.0f, 0.0f, 0.5f, 0.5f, PreviewAppearance.ScaleType.FIT));
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, watermarksetting, mProfile);

        mMediaStreamingManager.setStreamingStateListener(this);
        mMediaStreamingManager.setSurfaceTextureCallback(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
//        mMediaStreamingManager.setNativeLoggingEnabled(false);
        mMediaStreamingManager.setStreamStatusCallback(this);
        mMediaStreamingManager.setAudioSourceCallback(this);
        // update the StreamingProfile
//        mProfile.setStream(new Stream(mJSONObject1));
//        mMediaStreamingManager.setStreamingProfile(mProfile);
        setFocusAreaIndicator();
    }
}
