package com.medmeeting.m.zhiyi.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.LiveView.MyPayLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingEnrolActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import rx.Observer;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constant.WeChat_AppID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode
                + "\n" + getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode + " " + resp.errStr)));

//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode + " " + resp.errStr)));
//			builder.show();
//		}
        if (resp.errCode == -2) {

            //购买事件
            Data.getPurchaseEvent().setPurchaseSuccess(false);
            JAnalyticsInterface.onEvent(WXPayEntryActivity.this, Data.getPurchaseEvent());

            ToastUtils.show(this, "宝宝居然取消付费辣Ծ‸Ծ");
            finish();
        } else if (resp.errCode == 0) {
            ToastUtils.show(this, "宝宝已成功购票Ｏ(≧∇≦)Ｏ");

            //video pay
            if (Data.getPayType() == 1 && !Data.getTradeId().equals("")) {
                //通知后端，防止后端接受不到支付成功
                HttpData.getInstance().HttpDataUpdateLiveOrderStatus(new Observer<HttpResult3<Object, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "HttpDataUpdateLiveOrderStatus onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(WXPayEntryActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, Object> objectObjectHttpResult3) {
                        Log.e(TAG, "onNext");
                    }
                }, Data.getTradeId());
                startActivity(new Intent(this, VideoDetailActivity.class).putExtra("videoId", Data.getVideoId()));

            }else if (Data.getPayType() == 2 && !Data.getTradeId().equals("")) {
                MeetingEnrolActivity.reloadWebView();
            } else {
                HttpData.getInstance().HttpDataUpdateLiveOrderStatus(new Observer<HttpResult3<Object, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "HttpDataUpdateLiveOrderStatus onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(WXPayEntryActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, Object> objectObjectHttpResult3) {
                        Log.e(TAG, "onNext");
                    }
                }, Data.getTradeId());
                //*********我的订单页
                //*********我的订单页
                //*********我的订单页
                //*********我的订单页
                //*********我的订单页
                startActivity(new Intent(this, MyPayLiveRoomActivity.class));
            }


            //购买事件
            Data.getPurchaseEvent().setPurchaseSuccess(true);
            JAnalyticsInterface.onEvent(WXPayEntryActivity.this, Data.getPurchaseEvent());

            finish();
        }
    }
}
