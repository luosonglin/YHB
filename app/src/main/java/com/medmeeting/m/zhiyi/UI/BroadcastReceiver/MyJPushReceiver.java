package com.medmeeting.m.zhiyi.UI.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.IndexView.NewsActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import br.com.goncalves.pugnotification.notification.PugNotification;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class MyJPushReceiver extends BroadcastReceiver {
    private static String TAG = MyJPushReceiver.class.getName();
    private NotificationManager nm;

    public MyJPushReceiver() {
        Log.d(TAG, "啊啊啊");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        String id = "";
        String type = "";


        System.out.println(intent.getAction() + ", " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//            //send the Registration Id to your server...
//
//            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//
//            System.out.println("收到了自定义消息@@消息内容是:" + content);
//            System.out.println("收到了自定义消息@@消息extra是:" + extra);


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            System.out.println("收到了自定义消息@@消息内容是:" + content);
            System.out.println("收到了自定义消息@@消息extra是:" + extra);

            PugNotification.with(context)
                    .load()
                    .title("医会宝")
                    .message(content)
                    .smallIcon(R.mipmap.ic_launcher)//"https://wxt.sinaimg.cn/thumb300/a601622bly1fmzwrjm8cxj20u01e9488.jpg?tags=%5B%5D"
                    .largeIcon(R.mipmap.ic_launcher)
                    .flags(Notification.DEFAULT_ALL)
                    .simple()
                    .build();

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            // 在这里可以做些统计，或者做些其他工作

            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            System.out.println("收到了通知@@消息标题是:" + title);
            System.out.println("收到了通知@@消息内容是:" + content);
            System.out.println("收到了通知@@消息extra是:" + extra);

//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(extra);
//                id = jsonObject.getString("id");
//                type = jsonObject.getString("type");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context,
                    R.layout.customer_notitfication_layout,
                    R.id.icon,
                    R.id.title,
                    R.id.text);
            // 指定定制的 Notification Layout
            builder.statusBarDrawable = R.mipmap.logo;
            // 指定最顶层状态栏小图标
            builder.layoutIconDrawable = R.mipmap.ic_launcher;
            // 指定下拉状态栏时显示的通知图标

            JPushInterface.setPushNotificationBuilder(2, builder);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            // 在这里可以自己写代码去定义用户点击后的行为
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(extra);
                id = jsonObject.getString("id");
                type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(id)) return;
            switch (type) {
//                case "active":
//                    BrowserActivity.launch(context, "", "");
//                    break;
                case "live":
                    intent = new Intent(context, LiveProgramDetailActivity2.class);
                    intent.putExtra("programId", Integer.parseInt(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "video":
                    intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra("videoId", Integer.parseInt(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "event":
                    intent = new Intent(context, MeetingDetailActivity.class);
                    intent.putExtra("eventId", Integer.parseInt(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "blog":
                    intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("blogId", Integer.parseInt(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Log.i(TAG, "This message has no Extra data");
                        continue;
                    }

                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();

                        while (it.hasNext()) {
                            String myKey = it.next().toString();
                            sb.append("\nkey:" + key + ", value: [" +
                                    myKey + " - " + json.optString(myKey) + "]");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Get message extra JSON error!");
                    }

                    break;
                default:
                    sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
                    break;
            }
        }
        return sb.toString();
    }

}
