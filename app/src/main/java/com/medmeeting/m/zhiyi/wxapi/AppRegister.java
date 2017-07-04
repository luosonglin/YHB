package com.medmeeting.m.zhiyi.wxapi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

		msgApi.registerApp(Constant.WeChat_AppID);
	}
}
